package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

//    @Test
//    public void testInsert() {
//        IntStream.rangeClosed(1,100).forEach(i -> {
//            Board board = Board.builder()
//                    .title("title..." +i)
//                    .content("content..." + i)
//                    .writer("user"+ (i % 10))
//                    .build();
//
//            Board result = boardRepository.save(board);
//            log.info("BNO: " + result.getBno());
//        });


//        Board board = Board.builder().title("짬뽕 제목2").content("짬뽕 맛있다.2").writer("user1").build();
//
//        Board result = boardRepository.save(board);
//        log.info("BNO: " + result.getBno());
//    }

//    @Test
//    public void testSelect() {
//        Long bno = 100L;
//
//        Optional<Board> result = boardRepository.findById(bno);
//
//        Board board = result.orElseThrow();
//
//        log.info(board);
//
//    }

//    @Test
//    public void testUpdate() {
//
//        Long bno = 105L;
//
//        Optional<Board> result = boardRepository.findById(bno);
//
//        Board board = result.orElseThrow();
//
//        board.change("짬뽕 제목2", "짬뽕 맛있다.2");
//
//        boardRepository.save(board);
//
//    }

//    @Test
//    public void testDelete() {
//        Long bno = 1L;
//
//        boardRepository.deleteById(bno);
//    }

//    @Test
//    public void testPaging() {
//
//        //1 page order by bno desc
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
//
//        Page<Board> result = boardRepository.findAll(pageable);
//
//
//        log.info("total count: " + result.getTotalElements());
//        log.info("total pages:" + result.getTotalPages());
//        log.info("page number: " + result.getNumber());
//        log.info("page size: " + result.getSize());
//
//        List<Board> todoList = result.getContent();
//
//        todoList.forEach(board -> log.info(board));
//
//
//    }

//    @Test
//    public void testSearch1() {
//
//        // 2 page order by bno desc
//        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
//
//        Page<Board> page = boardRepository.search1(pageable);
//
//        log.info("결과 : " + page);
//    }

//    @Test
//    public void testSearchAll() {
//
//        String[] types = {"t", "c", "w"};
//
//        String keyword = "짬뽕";
//
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
//
//        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
//
//    }

//    @Test
//    public void testSearchAll2() {
//
//        String[] types = {"t", "c", "w"};
//
//        String keyword = "짬뽕";
//
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
//
//        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
//
//        //total pages
//        log.info(result.getTotalPages());
//
//        //pag size
//        log.info(result.getSize());
//
//        //pageNumber
//        log.info(result.getNumber());
//
//        //prev next
//        log.info(result.hasPrevious() + ": " + result.hasNext());
//
//        result.getContent().forEach(board -> log.info(board));
//    }

//    @Test
//    public void testSearchReplyCount() {
//        String[] types = {"t", "c", "w"};
//        String keyword = "1";
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
//
//        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);
//
//        log.info(result.getTotalPages());
//        log.info(result.getSize());
//        log.info(result.getNumber());
//        log.info(result.hasPrevious() + " : " + result.hasNext());
//        result.getContent().forEach(board -> log.info(board));
//    }

//    @Test
//    public void testInsertWithImages() {
//        Board board = Board.builder().title("Image Test").content("첨부파일 테스트").writer("tester").build();
//
//        for (int i = 0; i < 3; i++) {
//            board.addImage(UUID.randomUUID().toString(), "file" + i + "jpg");
//        }
//
//        boardRepository.save(board);
//    }

//    @Test
//    public void testReadWithImages() {
//        Optional<Board> result = boardRepository.findByIdWithImages(1L);
//        Board board = result.orElseThrow();
//
//        log.info(board);
//        log.info("-------------");
//        for (BoardImage boardImage : board.getImageSet()) {
//            log.info(boardImage);
//        }
//
//    }

    @Transactional
    @Commit
    @Test
    public void testModifyImages() {
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        // 기존의 첨부 파일들은 삭제
        board.clearImages();

        // 새로운 첨부 파일들

        for (int i = 0; i < 2; i++) {
            board.addImage(UUID.randomUUID().toString(), "updatefile" + i + "jgp");
        }

        boardRepository.save(board);

    }
}
