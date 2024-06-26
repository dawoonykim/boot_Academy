package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Reply;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {


//    @Autowired
//    private ReplyRepository replyRepository;


//    @Test
//    public void testInsert(){

//        Long bno = 100L;
//        Board board = Board.builder().bno(bno).build();
//        Reply reply=Reply.builder().board(board).replyText("댓글 연습1").replyer("user01").build();
//        replyRepository.save(reply);

//        Long bno = 100L;
//
//        IntStream.rangeClosed(1, 20).forEach(i -> {
//            Board board = Board.builder().bno(bno).build();
//            Reply reply=Reply.builder().board(board).replyText("댓글 연습"+i).replyer("user"+i%5).build();
//            replyRepository.save(reply);
//        });
//    }

//    @Test
//    @Transactional
//    public void testBoardReplies(){
//        Long bno = 100L;
//
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
//        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
//
//        log.info("#################");
//        log.info(result.getContent());
//        result.getContent().forEach(reply -> {
//            log.info("Reply : "+reply);
//        });
//    }


}
