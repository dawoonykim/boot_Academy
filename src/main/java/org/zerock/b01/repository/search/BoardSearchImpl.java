package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Fetchable;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;
import org.zerock.b01.domain.QReply;
import org.zerock.b01.dto.BoardImageDTO;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board); // select ... from board

        BooleanBuilder booleanBuilder = new BooleanBuilder();
//        query.where(board.title.contains("1"));
        booleanBuilder.or(board.title.contains("11"));
        booleanBuilder.or(board.content.contains("11"));

        query.where(booleanBuilder);
        query.where(board.bno.gt(0L));
        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return null;

    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }
        query.where(board.bno.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);

        // 게시물과 댓글의 경우 한 쪽에만 데이터가 존재하는 상황이 발생
        // 그래서 outer join을 통해서 처리함

        query.leftJoin(reply).on(reply.board.eq(board));

        //  조인 후 게시물 당 처리가 필요하므로 groupBy를 적용
        query.groupBy(board);

        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }

        query.where(board.bno.gt(0L));
        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(BoardListReplyCountDTO.class, board.bno, board.title, board.writer, board.regDate, reply.count().as("replyCount")));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);
        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

//    @Override
//    public Page<BoardListReplyCountDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
//
//        QBoard board = QBoard.board;
//        QReply reply = QReply.reply;
//
//        JPQLQuery<Board> boardJPQLQuery = from(board);
//        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));
//
//        getQuerydsl().applyPagination(pageable, boardJPQLQuery);
//
//        List<Board> boardList = boardJPQLQuery.fetch();
//
//        boardList.forEach(board1 -> {
//            log.info(board1.getBno());
//            log.info(board1.getImageSet());
//            log.info("---------------------");
//        });
//
//        return null;
//    }


//    @Override
//    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
//
//        QBoard board = QBoard.board;
//        QReply reply = QReply.reply;
//
//        JPQLQuery<Board> boardJPQLQuery = from(board);
//        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));
//
//        boardJPQLQuery.groupBy(board);
//
//        getQuerydsl().applyPagination(pageable, boardJPQLQuery);
//
//        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());
//
//        List<Tuple> tupleList = tupleJPQLQuery.fetch();
//
//        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
//            Board board1 = tuple.get(board);
//            long replyCount = tuple.get(1, Long.class);
//
//            BoardListAllDTO dto = BoardListAllDTO.builder().bno(board1.getBno()).title(board1.getTitle()).writer(board1.getWriter()).regDate(LocalDate.from(board1.getRegDate())).replyCount(replyCount).build();
//            return dto;
//        }).collect(Collectors.toList());
//
//        long totalCount = boardJPQLQuery.fetchCount();
//        return new PageImpl<>(dtoList, pageable, totalCount);
//    }

//    @Override
//    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
//
//        QBoard board = QBoard.board;
//        QReply reply = QReply.reply;
//
//        JPQLQuery<Board> boardJPQLQuery = from(board);
//        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));
//
//        boardJPQLQuery.groupBy(board);
//
//        getQuerydsl().applyPagination(pageable, boardJPQLQuery);
//
//        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());
//
//        List<Tuple> tupleList = tupleJPQLQuery.fetch();
//
//        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
//            Board board1 = tuple.get(board);
//            long replyCount = tuple.get(1, Long.class);
//
//            BoardListAllDTO dto = BoardListAllDTO.builder()
//                    .bno(board1.getBno())
//                    .title(board1.getTitle())
//                    .writer(board1.getWriter())
//                    .regDate(LocalDate.from(board1.getRegDate()))
//                    .replyCount(replyCount)
//                    .build();
//
//            // BoardImage를 BoardImageDTO로 처리할 부분
//
//            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
//                    .map(boardImage ->
//                            BoardImageDTO.builder()
//                                    .uuid(boardImage.getUuid())
//                                    .fileName(boardImage.getFileName())
//                                    .ord(boardImage.getOrd())
//                                    .build()
//                    ).collect(Collectors.toList());
//            dto.setBoardImages(imageDTOS);
//            return dto;
//        }).collect(Collectors.toList());
//
//        long totalCount = boardJPQLQuery.fetchCount();
//        return new PageImpl<>(dtoList, pageable, totalCount);
//    }

    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));

        if ((types != null && types.length != 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            boardJPQLQuery.where(booleanBuilder);
        }
        boardJPQLQuery.groupBy(board);

        getQuerydsl().applyPagination(pageable, boardJPQLQuery);

        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());

        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            Board board1 = tuple.get(board);
            long replyCount = tuple.get(1, Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(LocalDate.from(board1.getRegDate()))
                    .replyCount(replyCount)
                    .build();

            // BoardImage를 BoardImageDTO로 처리할 부분

            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage ->
                            BoardImageDTO.builder()
                                    .uuid(boardImage.getUuid())
                                    .fileName(boardImage.getFileName())
                                    .ord(boardImage.getOrd())
                                    .build()
                    ).collect(Collectors.toList());
            dto.setBoardImages(imageDTOS);
            return dto;
        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();
        return new PageImpl<>(dtoList, pageable, totalCount);
    }
}
