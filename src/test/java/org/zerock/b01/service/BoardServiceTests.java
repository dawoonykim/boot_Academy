package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.dto.*;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;
//
//    @Test
//    public void testRegister() {
////
////        log.info(boardService.getClass().getName());
////
////        BoardDTO boardDTO = BoardDTO.builder()
////                .title("Sample Title...")
////                .content("Sample Content...")
////                .writer("user00")
////                .build();
////
////        Long bno = boardService.register(boardDTO);
////
////        log.info("bno: " + bno);
//
//        IntStream.rangeClosed(1, 100).forEach(i -> {
//            BoardDTO boardDTO = BoardDTO.builder().
//                    title("Sample Title" + i).
//                    content("Sample Content" + i).
//                    writer("user" + i / 10).
//                    build();
//
//            Long bno = boardService.register(boardDTO);
//
//            log.info("bno: " + bno);
//        });
//    }
//    @Test
//    public void testModify() {
//
//        //변경에 필요한 데이터만
//        BoardDTO boardDTO = BoardDTO.builder()
//                .bno(101L)
//                .title("Updated....101")
//                .content("Updated content 101...")
//                .build();
//
//        boardService.modify(boardDTO);
//
//    }

//    @Test
//    public void testList() {
//
//        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
//                .type("tcw")
//                .keyword("1")
//                .page(1)
//                .size(10)
//                .build();
//
//        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
//
//        log.info(responseDTO);
//
//    }

    @Test
    public void testReadAll() {
        Long bno = 106L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for (String fileName : boardDTO.getFileNames()) {
            log.info(fileName);
        }
    }

    @Test
    public void testModify() {

        //변경에 필요한 데이터만
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(106L)
                .title("Updated....106")
                .content("Updated content 106...")
                .build();

        boardDTO.setFileNames(List.of(UUID.randomUUID() + "_zzz.jpg"));

        boardService.modify(boardDTO);

    }

    @Test
    public void testRemoveAll() {
        Long bno = 1L;
        boardService.remove(bno);
    }

    @Test
    public void testListWithAll() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);
        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno() + ":" + boardListAllDTO.getTitle());

            if (boardListAllDTO.getBoardImages() != null) {
                for (BoardImageDTO boardImage : boardListAllDTO.getBoardImages()) {
                    log.info(boardImage);
                }
            }
            log.info("----------------------------");
        });
    }

}