package scanly.io.scanly_back.cardbook.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import scanly.io.scanly_back.IntegrationTestSupport;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.card.domain.CardRepository;
import scanly.io.scanly_back.card.infrastructure.CardJpaRepository;
import scanly.io.scanly_back.cardbook.application.dto.command.*;
import scanly.io.scanly_back.cardbook.application.dto.info.CardBookInfo;
import scanly.io.scanly_back.cardbook.application.dto.info.CardBookPreviewInfo;
import scanly.io.scanly_back.cardbook.application.dto.info.CardExchangeInfo;
import scanly.io.scanly_back.cardbook.application.dto.info.RegisterCardBookInfo;
import scanly.io.scanly_back.cardbook.domain.*;
import scanly.io.scanly_back.cardbook.domain.event.CardExchangedEvent;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;
import scanly.io.scanly_back.cardbook.infrastructure.*;
import scanly.io.scanly_back.cardbook.infrastructure.entity.CardBookEntity;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;
import scanly.io.scanly_back.common.ratelimit.RateLimiterService;
import scanly.io.scanly_back.member.domain.Member;
import scanly.io.scanly_back.member.domain.MemberRepository;
import scanly.io.scanly_back.member.infrastructure.MemberJpaRepository;
import scanly.io.scanly_back.notification.application.CardExchangeNotificationListener;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("CardBookService 테스트")
class CardBookServiceTest extends IntegrationTestSupport {

    @Autowired
    private CardBookService cardBookService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardBookRepository cardBookRepository;

    @Autowired
    private CardJpaRepository cardJpaRepository;

    @Autowired
    private CardBookJpaRepository cardBookJpaRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagJpaRepository tagJpaRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupJpaRepository groupJpaRepository;

    @Autowired
    private CardExchangeRepository cardExchangeRepository;

    @Autowired
    private CardExchangeJpaRepository cardExchangeJpaRepository;

    @MockitoBean
    private RateLimiterService rateLimiterService;

    @MockitoBean
    private CardExchangeNotificationListener cardExchangeNotificationListener;

    @Autowired
    private CardBookMapper cardBookMapper;

    @AfterEach
    void after() {
        tagJpaRepository.deleteAllInBatch();
        cardJpaRepository.deleteAllInBatch();
        cardExchangeJpaRepository.deleteAllInBatch();
        cardBookJpaRepository.deleteAllInBatch();
        groupJpaRepository.deleteAllInBatch();
        memberJpaRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("명함첩 저장 검증")
    class Save {

        @Test
        @DisplayName("[Happy] 명함을 저장한다.")
        void save() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCard();
            Card savedCard = cardRepository.save(card);

            SaveCardBookCommand command
                    = new SaveCardBookCommand(memberId, savedCard.getId(), null);

            // when
            RegisterCardBookInfo info = cardBookService.save(command);

            // then
            assertThat(info)
                    .extracting(
                            "cardId", "name", "title", "company", "groupId"
                    ).contains(
                            savedCard.getId(), savedCard.getName(), savedCard.getTitle(), savedCard.getCompany(), command.groupId()
                    );
            assertThat(info.profileSnapshot())
                    .extracting(
                            "name", "title", "company", "phone", "email",
                            "bio", "profileImageUrl", "portfolioUrl", "location"
                    ).contains(
                            savedCard.getName(), savedCard.getTitle(), savedCard.getCompany(), savedCard.getPhone(), savedCard.getEmail(),
                            savedCard.getBio(), savedCard.getProfileImageUrl(), savedCard.getPortfolioUrl(), savedCard.getLocation()
                    );
        }

        @Test
        @DisplayName("[Bad] 존재하지 않은 명함을 저장할 경우 실패한다.")
        void cardNotFound() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCard();
            Card savedCard = cardRepository.save(card);

            SaveCardBookCommand command
                    = new SaveCardBookCommand(memberId, "card-123", null);

            // when & then
            assertThatThrownBy(() -> cardBookService.save(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CARD_NOT_FOUND);
        }

        @Test
        @DisplayName("[Bad] 자신의 명함을 저장하려고 하면 실패한다.")
        void ownCardSave() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createdCardWithMemberId(memberId);
            Card savedCard = cardRepository.save(card);

            SaveCardBookCommand command
                    = new SaveCardBookCommand(memberId, savedCard.getId(), null);

            // when & then
            assertThatThrownBy(() -> cardBookService.save(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CANNOT_SAVE_OWN_CARD);
        }

        @Test
        @DisplayName("[Bad] 명함첩을 중복으로 저장하려고 하면 실패한다.")
        void duplicateSave() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCard();
            Card savedCard = cardRepository.save(card);
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, savedCard);
            cardBookRepository.save(cardBook);

            SaveCardBookCommand command
                    = new SaveCardBookCommand(memberId, savedCard.getId(), null);

            // when & then
            assertThatThrownBy(() -> cardBookService.save(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CARD_BOOK_ALREADY_EXISTS);
        }
    }

    @Nested
    @DisplayName("명함 교환 검증")
    class Exchange {
        @Test
        @DisplayName("[Happy] 명함을 교환한다.")
        void cardExchange() {
            // given
            Member member = crateMember();
            Member sender = memberRepository.save(member);
            Card senderCard = createdCardWithMemberId(sender.getId());
            cardRepository.save(senderCard);

            Card receiverCard = createCard();
            Card savedReceiverCard = cardRepository.save(receiverCard);

            CardExchangeCommand command = new CardExchangeCommand(sender.getId(), savedReceiverCard.getId());

            given(rateLimiterService.isDailyExchangeAllowed(any(), any(), anyInt()))
                    .willReturn(true);

            // when
            CardExchangeInfo info = cardBookService.cardExchange(command);

            // then
            assertThat(info)
                    .extracting("senderId", "receiverId")
                    .contains(sender.getId(), savedReceiverCard.getMemberId());

            verify(cardExchangeNotificationListener).handle(any(CardExchangedEvent.class));
        }

        @Test
        @DisplayName("[Bad] 존재하지 않는 명함으로 교환 시도 시 실패한다.")
        void cardNotFound() {
            // given
            Member member = crateMember();
            Member sender = memberRepository.save(member);

            CardExchangeCommand command = new CardExchangeCommand(sender.getId(), "card-test");

            // when & then
            assertThatThrownBy(() -> cardBookService.cardExchange(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CARD_NOT_FOUND);
        }

        @Test
        @DisplayName("[Bad] 일일 명함 교환 제한 초과 시 실패한다.")
        void dailyLimit() {
            // given
            Member member = crateMember();
            Member sender = memberRepository.save(member);
            Card senderCard = createdCardWithMemberId(sender.getId());
            cardRepository.save(senderCard);

            Card receiverCard = createCard();
            Card savedReceiverCard = cardRepository.save(receiverCard);

            CardExchangeCommand command = new CardExchangeCommand(sender.getId(), savedReceiverCard.getId());

            given(rateLimiterService.isDailyExchangeAllowed(any(), any(), anyInt()))
                    .willReturn(false);

            // when & then
            assertThatThrownBy(() -> cardBookService.cardExchange(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.DAILY_EXCHANGE_LIMIT_EXCEEDED);
        }

        @Test
        @DisplayName("[Bad] 자신의 명함으로 교환 시도 시 실패한다.")
        void cannotExchangeOwnCard() {
            // given
            Member member = crateMember();
            Member sender = memberRepository.save(member);
            String senderId = sender.getId();
            Card card = createdCardWithMemberId(senderId);
            Card savedCard = cardRepository.save(card);

            CardExchangeCommand command = new CardExchangeCommand(senderId, savedCard.getId());

            given(rateLimiterService.isDailyExchangeAllowed(any(), any(), anyInt()))
                    .willReturn(false);

            // when & then
            assertThatThrownBy(() -> cardBookService.cardExchange(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CANNOT_EXCHANGE_OWN_CARD);
        }

        @Test
        @DisplayName("[Happy] 명함 교환을 수락한다.")
        void acceptExchange() {
            // given
            Member sender = memberRepository.save(crateMember());
            Member receiver = memberRepository.save(Member.signUP("test2", "test", "password123", "test2@test.com"));

            String senderId = sender.getId();
            String receiverId = receiver.getId();
            Card senderCard = cardRepository.save(createdCardWithMemberId(senderId));
            Card receiverCard = cardRepository.save(createdCardWithMemberId(receiverId));

            CardExchange cardExchange = createCardExchange(senderId, receiverId);
            CardExchange savedCardExchange = cardExchangeRepository.save(cardExchange);
            String cardExchangeId = savedCardExchange.getId();

            AcceptExchangeCommand command
                    = new AcceptExchangeCommand(cardExchangeId, receiverId);

            // when
            RegisterCardBookInfo info = cardBookService.acceptExchange(command);

            // then
            Optional<CardBook> cardBookOpt = cardBookJpaRepository.findByMemberIdAndCardId(receiverId, senderCard.getId()).map(cardBookMapper::toDomain);
            assertThat(cardBookOpt).isPresent();

            ProfileSnapshot profileSnapshot = cardBookOpt.get().getProfileSnapshot();
            assertThat(info)
                    .extracting(
                            "cardId", "name", "title", "company"
                    ).contains(
                            senderCard.getId(), senderCard.getName(), senderCard.getTitle(), senderCard.getCompany()
                    );
            assertThat(info.profileSnapshot())
                    .extracting(
                            "name", "title", "company", "phone", "email",
                            "bio", "profileImageUrl", "portfolioUrl", "location"
                    ).contains(
                            profileSnapshot.name(), profileSnapshot.title(), profileSnapshot.company(), profileSnapshot.phone(), profileSnapshot.email(),
                            profileSnapshot.bio(), profileSnapshot.profileImageUrl(), profileSnapshot.portfolioUrl(), profileSnapshot.location()
                    );
        }

        @Test
        @DisplayName("[Bad] 명함 교환을 수락 시 명함 교환 내역의 상태가 대기 중이 아닐 경우 실패한다.")
        void acceptExchangeWhenAlreadyProcessed() {
            // given
            Member sender = memberRepository.save(crateMember());
            Member receiver = memberRepository.save(Member.signUP("test2", "test", "password123", "test2@test.com"));

            String senderId = sender.getId();
            String receiverId = receiver.getId();
            Card senderCard = cardRepository.save(createdCardWithMemberId(senderId));
            Card receiverCard = cardRepository.save(createdCardWithMemberId(receiverId));

            AcceptExchangeCommand command
                    = new AcceptExchangeCommand(UUID.randomUUID().toString(), receiverId);

            // when & then
            assertThatThrownBy(() -> cardBookService.acceptExchange(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.EXCHANGE_NOT_FOUND);
        }

        @Test
        @DisplayName("[Bad] 명함 교환을 수락 시 명함 교환 내역이 존재하지 않으면 실패한다.")
        void acceptExchangeWhenNotFoundCardExchange() {
            // given
            Member sender = memberRepository.save(crateMember());
            Member receiver = memberRepository.save(Member.signUP("test2", "test", "password123", "test2@test.com"));

            String senderId = sender.getId();
            String receiverId = receiver.getId();
            Card senderCard = cardRepository.save(createdCardWithMemberId(senderId));
            Card receiverCard = cardRepository.save(createdCardWithMemberId(receiverId));

            CardExchange cardExchange = createCardExchange(senderId, receiverId);
            cardExchange.accept();
            CardExchange savedCardExchange = cardExchangeRepository.save(cardExchange);
            String cardExchangeId = savedCardExchange.getId();

            AcceptExchangeCommand command
                    = new AcceptExchangeCommand(cardExchangeId, receiverId);

            // when & then
            assertThatThrownBy(() -> cardBookService.acceptExchange(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.EXCHANGE_ALREADY_PROCESSED);
        }

        @Test
        @DisplayName("[Bad] 명함 교환을 수락 시 발신자(명함 교환 요청자)의 명함이 존재하지 않으면 실패한다.")
        void acceptExchangeWhenSenderCardNotFound() {
            // given
            Member sender = memberRepository.save(crateMember());
            Member receiver = memberRepository.save(Member.signUP("test2", "test", "password123", "test2@test.com"));

            String senderId = sender.getId();
            String receiverId = receiver.getId();
            Card receiverCard = cardRepository.save(createdCardWithMemberId(receiverId));

            CardExchange cardExchange = createCardExchange(senderId, receiverId);
            CardExchange savedCardExchange = cardExchangeRepository.save(cardExchange);
            String cardExchangeId = savedCardExchange.getId();

            AcceptExchangeCommand command
                    = new AcceptExchangeCommand(cardExchangeId, receiverId);

            // when & then
            assertThatThrownBy(() -> cardBookService.acceptExchange(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CARD_NOT_FOUND);
        }


        @Test
        @DisplayName("[Bad] 명함 교환을 수락 시 이미 명함첩에 저장된 명함일 경우 실패한다.")
        void acceptExchangeWhenAlreadyExists() {
            // given
            Member sender = memberRepository.save(crateMember());
            Member receiver = memberRepository.save(Member.signUP("test2", "test", "password123", "test2@test.com"));

            String senderId = sender.getId();
            Card senderCard = cardRepository.save(createdCardWithMemberId(senderId));
            String receiverId = receiver.getId();
            Card receiverCard = cardRepository.save(createdCardWithMemberId(receiverId));

            CardExchange cardExchange = createCardExchange(senderId, receiverId);
            CardExchange savedCardExchange = cardExchangeRepository.save(cardExchange);
            String cardExchangeId = savedCardExchange.getId();

            CardBook cardBook = createCardBookWithMemberIdAndCard(receiverId, senderCard);
            cardBookRepository.save(cardBook);

            AcceptExchangeCommand command
                    = new AcceptExchangeCommand(cardExchangeId, receiverId);


            // when & then
            assertThatThrownBy(() -> cardBookService.acceptExchange(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CARD_BOOK_ALREADY_EXISTS);
        }
    }

    @Nested
    @DisplayName("명함첩 조회 검증")
    class Read {

        @Test
        @DisplayName("[Happy] 회원아이디, 명함첩 그룹 아이디, 페이징 정보로 명함첩 목록을 조회한다.")
        void readAll() {
            // given
            String memberId = UUID.randomUUID().toString();
            CardBook cardBook1 = createCardBookWithMemberIdAndCard(memberId, createCard());
            CardBook cardBook2 = createCardBookWithMemberIdAndCard(memberId, createCard());
            List<CardBook> cardBooks = cardBookRepository.saveAll(List.of(cardBook1, cardBook2));
            Pageable pageable = Pageable.ofSize(5);

            // when
            Page<CardBookPreviewInfo> infos = cardBookService.readAll(memberId, null, pageable);

            // then
            assertThat(infos).hasSize(2);
            CardBook first = cardBooks.getFirst();
            CardBook last = cardBooks.getLast();
            assertThat(infos.getContent())
                    .extracting(
                            "id", "cardId", "name", "title", "company",
                            "profileImageUrl", "groupId", "memo", "isFavorite"
                    )
                    .containsExactlyInAnyOrder(
                            tuple(
                                    first.getId(), first.getCardId(), first.getProfileSnapshot().name(), first.getProfileSnapshot().title(), first.getProfileSnapshot().company(),
                                    first.getProfileSnapshot().profileImageUrl(), first.getGroupId(), first.getMemo(), first.isFavorite()
                            ),
                            tuple(
                                    last.getId(), last.getCardId(), last.getProfileSnapshot().name(), last.getProfileSnapshot().title(), last.getProfileSnapshot().company(),
                                    last.getProfileSnapshot().profileImageUrl(), last.getGroupId(), last.getMemo(), last.isFavorite()
                            )
                    );
        }

        @Test
        @DisplayName("[Happy] 명함첩 상세 조회")
        void read() {
            // given
            String memberId = UUID.randomUUID().toString();
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, createCard());
            CardBook savedCardBook = cardBookRepository.save(cardBook);
            Tag tag = createTag(savedCardBook.getId());
            Tag savedTag = tagRepository.save(tag);

            // when
            CardBookInfo info = cardBookService.read(memberId, savedCardBook.getId());

            // then
            assertThat(info)
                    .extracting(
                            "id", "cardId", "name", "title", "company",
                            "profileImageUrl", "groupId", "memo", "isFavorite"
                    ).contains(
                            savedCardBook.getId(), savedCardBook.getCardId(), savedCardBook.getProfileSnapshot().name(), savedCardBook.getProfileSnapshot().title(), savedCardBook.getProfileSnapshot().company(),
                            savedCardBook.getProfileSnapshot().profileImageUrl(), savedCardBook.getGroupId(), savedCardBook.getMemo(), savedCardBook.isFavorite()
                    );
            assertThat(info.tagList())
                    .extracting("id", "cardBookId", "name")
                    .containsExactlyInAnyOrder(
                            tuple(savedTag.getId(), savedTag.getCardBookId(), savedTag.getName())
                    );
        }

        @Test
        @DisplayName("[Happy] 명함첩이 존재하면 true를 리턴한다.")
        void existsTrue() {
            // given
            String memberId = UUID.randomUUID().toString();
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, createCard());
            CardBook savedCardBook = cardBookRepository.save(cardBook);

            // when
            boolean result = cardBookService.exists(memberId, savedCardBook.getCardId());

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("[Happy] 명함첩이 존재하지 않으면 false를 리턴한다.")
        void existsFalse() {
            // given
            String memberId = UUID.randomUUID().toString();

            // when
            boolean result = cardBookService.exists(memberId, "card-id");

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("명함첩 수정 검증")
    class Update {

        @Test
        @DisplayName("[Happy] 명함첩 그룹을 수정한다.")
        void updateGroup() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCard();
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, card);
            CardBook savedCardBook = cardBookRepository.save(cardBook);
            Group group = createGroup();
            Group savedGroup = groupRepository.save(group);
            String newGroupId = savedGroup.getId();

            UpdateCardBookGroupCommand command
                    = new UpdateCardBookGroupCommand(
                    savedCardBook.getId(),
                    newGroupId,
                    memberId
            );

            // when
            CardBookInfo info = cardBookService.updateGroup(command);

            // then
            assertThat(info.groupId()).isEqualTo(newGroupId);
        }

        @Test
        @DisplayName("[Bad] 명함첩 그룹이 존재하지 않으면 명함첩 그룹 수정 시 오류가 발생한다.")
        void updateGroupWhenGroupIdNotFound() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCard();
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, card);
            CardBook savedCardBook = cardBookRepository.save(cardBook);
            String newGroupId = UUID.randomUUID().toString();

            UpdateCardBookGroupCommand command
                    = new UpdateCardBookGroupCommand(
                    savedCardBook.getId(),
                    newGroupId,
                    memberId
            );

            // when & then
            assertThatThrownBy(() -> cardBookService.updateGroup(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.GROUP_NOT_FOUND);
        }

        @Test
        @DisplayName("[Happy] 명함첩 메모를 수정한다.")
        void updateMemo() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCard();
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, card);
            CardBook savedCardBook = cardBookRepository.save(cardBook);
            String newMemo = "신규 메모";

            UpdateCardBookMemoCommand command
                    = new UpdateCardBookMemoCommand(
                    savedCardBook.getId(),
                    newMemo,
                    memberId
            );

            // when
            CardBookInfo info = cardBookService.updateMemo(command);

            // then
            assertThat(info.memo()).isEqualTo(newMemo);
        }

        @Test
        @DisplayName("[Happy] 명함첩 즐겨찾기를 수정한다.")
        void updateFavorite() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCard();
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, card);
            CardBook savedCardBook = cardBookRepository.save(cardBook);
            boolean isFavorite = true;

            UpdateCardBookFavoriteCommand command
                    = new UpdateCardBookFavoriteCommand(
                    savedCardBook.getId(),
                    isFavorite,
                    memberId
            );

            // when
            CardBookInfo info = cardBookService.updateFavorite(command);

            // then
            assertThat(info.isFavorite()).isTrue();
        }

        @Test
        @DisplayName("[Happy] 명함첩 프로필 스냅샷을 최신화한다.")
        void refreshSnapshot() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCard();
            Card savedCard = cardRepository.save(card);
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, savedCard);
            CardBook savedCardBook = cardBookRepository.save(cardBook);
            String cardBookId = savedCardBook.getId();

            // when
            cardBookService.refreshSnapshot(memberId, cardBookId);

            // then
            Optional<CardBook> updatedCardBook = cardBookRepository.findByIdAndMemberId(cardBookId, memberId);
            assertThat(updatedCardBook).isPresent();

            ProfileSnapshot profileSnapshot = updatedCardBook.get().getProfileSnapshot();
            assertThat(profileSnapshot)
                    .extracting(
                            "name", "title", "company", "phone", "email",
                            "bio", "profileImageUrl", "portfolioUrl", "location"
                    ).contains(
                            savedCard.getName(), savedCard.getTitle(), savedCard.getCompany(), savedCard.getPhone(), savedCard.getEmail(),
                            savedCard.getBio(), savedCard.getProfileImageUrl(), savedCard.getPortfolioUrl(), savedCard.getLocation()
                    );
        }

        @Test
        @DisplayName("[Bad] 명함첩 프로필 스냅샷 최신화 시 원본 명함이 존재하지 않으면 오류가 발생한다.")
        void refreshSnapshotWhenNotFoundOriginalCard() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCardWithId();
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, card);
            CardBook savedCardBook = cardBookRepository.save(cardBook);
            String cardBookId = savedCardBook.getId();

            // when & then
            assertThatThrownBy(() -> cardBookService.refreshSnapshot(memberId, cardBookId))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.ORIGINAL_CARD_DELETED);
        }

        @Test
        @DisplayName("[Happy] 명함첩의 명함 아이디를 null로 변경한다.")
        void clearCardId() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCard();
            Card savedCard = cardRepository.save(card);
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, savedCard);
            cardBookRepository.save(cardBook);

            String cardId = savedCard.getId();

            // when
            cardBookService.clearCardId(cardId);

            // then
            List<CardBook> cardBooks
                    = cardBookJpaRepository.findAllByCardId(cardId).stream().map(cardBookMapper::toDomain).toList();
            assertThat(cardBooks).isEmpty();
        }
    }

    @Nested
    @DisplayName("명함첩 삭제 검증")
    class Delete {

        @Test
        @DisplayName("[Happy] 명함첩을 삭제한다.")
        void delete() {
            // given
            String memberId = UUID.randomUUID().toString();
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, createCard());
            CardBook savedCardBook = cardBookRepository.save(cardBook);
            String cardBookId = savedCardBook.getId();
            Tag tag = createTag(cardBookId);
            tagRepository.save(tag);

            // when
            cardBookService.delete(memberId, cardBookId);

            // then
            Optional<CardBook> foundCardBook = cardBookRepository.findByIdAndMemberId(cardBookId, memberId);
            List<Tag> tagList = tagRepository.findAllByCardBookId(cardBookId);

            assertThat(foundCardBook).isNotPresent();
            assertThat(tagList).isEmpty();
        }

        @Test
        @DisplayName("")
        void deleteAllByMemberId() {
            // given
            String memberId = UUID.randomUUID().toString();
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, createCard());
            CardBook savedCardBook = cardBookRepository.save(cardBook);
            String cardBookId = savedCardBook.getId();
            Tag tag = createTag(cardBookId);
            tagRepository.save(tag);

            // when
            cardBookService.deleteAllByMemberId(memberId);

            // then
            List<CardBook> cardBooks = cardBookRepository.findAllByMemberId(memberId);
            assertThat(cardBooks).isEmpty();
        }
    }

    private CardExchange createCardExchange(String senderId, String receiverId) {
        return CardExchange.create(senderId, receiverId);
    }

    private Member crateMember() {
        return Member.signUP(
                "test",
                "test",
                "test1234",
                "test@test.com"
        );
    }

    private CardBook createCardBookWithMemberIdAndCard(String memberId, Card card) {
        return CardBook.create(
                memberId,
                card.getId(),
                ProfileSnapshot.from(card),
                null
        );
    }

    private Card createdCardWithMemberId(String memberId) {
        return Card.create(
                memberId,
                "이름",
                "타이틀",
                "회사",
                "01012341234",
                "test@test.com",
                null,
                null,
                null,
                null
        );
    }

    private Card createCardWithId() {
        return Card.of(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "이름",
                "타이틀",
                "회사",
                "01012341234",
                "test@test.com",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private Card createCard() {
        return Card.create(
                UUID.randomUUID().toString(),
                "이름",
                "타이틀",
                "회사",
                "01012341234",
                "test@test.com",
                null,
                null,
                null,
                null
        );
    }

    private Tag createTag(String cardBookId) {
        return Tag.create(
                cardBookId,
                "태그명"
        );
    }

    private Group createGroup() {
        return Group.create(
                UUID.randomUUID().toString(),
                "그룹명",
                0
        );
    }
}
