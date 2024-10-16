package com.nbacm.trelldochi.domain.board.repository;

import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByWorkSpace(WorkSpace workSpace);

    List<Board> findByWorkSpace(WorkSpace workspace);

}
