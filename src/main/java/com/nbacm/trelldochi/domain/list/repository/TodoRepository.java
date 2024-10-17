package com.nbacm.trelldochi.domain.list.repository;

import com.nbacm.trelldochi.domain.list.entity.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoList, Long> {
    List<TodoList> findByBoardId(Long boardId);

    // 순서변경 메서드
    @Modifying
    @Query("update TodoList t set t.listOrder = t.listOrder+1 where t.listOrder between :startOrder and :endOrder")
    void incrementOrderBetween(@Param("startOrder") int startOrder, @Param("endOrder") int endOrder);

    @Modifying
    @Query("update TodoList t set t.listOrder = t.listOrder-1 where t.listOrder between :startOrder and :endOrder")
    void decrementOrderBetween(@Param("startOrder") int startOrder, @Param("endOrder") int endOrder);

    @Query("select coalesce(max(t.listOrder), 0) from TodoList t where t.board.id = :boardId")
    int findMaxListOrderByBoardId(@Param("boardId") Long boardId);
}
