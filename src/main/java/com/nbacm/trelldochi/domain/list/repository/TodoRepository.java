package com.nbacm.trelldochi.domain.list.repository;

import com.nbacm.trelldochi.domain.list.entity.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoList, Long> {
    List<TodoList> findByBoardId(Long boardId);
}
