package com.nbacm.trelldochi.domain.card.repository;

import com.nbacm.trelldochi.domain.card.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, CardDslRepository {


    @Query(value = """
                SELECT c.* 
                FROM card c 
                JOIN todolist t ON t.id = c.todolist_id 
                JOIN board b ON b.id = t.board_id
                LEFT JOIN card_manager cm ON cm.card_id = c.card_id
                LEFT JOIN user u ON u.id = cm.user_id
                WHERE c.is_deleted = false 
                AND CASE 
                    WHEN :title IS NOT NULL 
                    THEN MATCH(c.title) AGAINST(:title IN NATURAL LANGUAGE MODE)
                    ELSE 1=1 
                END
                AND CASE 
                    WHEN :explanation IS NOT NULL 
                    THEN MATCH(c.explanation) AGAINST(:explanation IN NATURAL LANGUAGE MODE)
                    ELSE 1=1 
                END
                AND (:deadline IS NULL OR c.deadline = :deadline)
                AND (:managerName IS NULL OR u.nickname = :managerName)
                AND (:boardId IS NULL OR b.id = :boardId)
            """,
            countQuery = """
                        SELECT COUNT(*)
                        FROM card c 
                        JOIN todolist t ON t.id = c.todolist_id 
                        JOIN board b ON b.id = t.board_id
                        LEFT JOIN card_manager cm ON cm.card_id = c.card_id
                        LEFT JOIN user u ON u.id = cm.user_id
                        WHERE c.is_deleted = false 
                        AND CASE 
                            WHEN :title IS NOT NULL 
                            THEN MATCH(c.title) AGAINST(:title IN NATURAL LANGUAGE MODE)
                            ELSE 1= 1 
                        END
                        AND CASE 
                            WHEN :explanation IS NOT NULL 
                            THEN MATCH(c.explanation) AGAINST(:explanation IN NATURAL LANGUAGE MODE)
                            ELSE 1=1 
                        END
                        AND (:deadline IS NULL OR c.deadline = :deadline)
                        AND (:managerName IS NULL OR u.nickname = :managerName)
                        AND (:boardId IS NULL OR b.id = :boardId)
                    """,
            nativeQuery = true)
    Page<Card> searchCardPaginationByFilter(@Param("title") String title,
                                            @Param("explanation") String explanation,
                                            @Param("deadline") LocalDate deadline,
                                            @Param("managerName") String managerName,
                                            @Param("boardId") Long boardId,
                                            Pageable pageable);
}
