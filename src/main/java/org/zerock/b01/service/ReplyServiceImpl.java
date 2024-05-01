package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Reply;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.repository.BoardRepository;
import org.zerock.b01.repository.ReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.zerock.b01.domain.Board;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    private final ModelMapper modelMapper;

    private final BoardRepository boardRepository;
    ;
    @Override
    public Long register(ReplyDTO replyDTO) {

        Reply reply = modelMapper.map(replyDTO, Reply.class);
        log.info("ReplyServiceImpl register Reply : " + reply);

        // ReplyDTO의 bno를 사용하여 board 엔티티를 찾아서 설정
        Board board = boardRepository.findById(replyDTO.getBno()).orElseThrow(() -> new IllegalArgumentException("Invalid bno"));
        reply.setBoard(board);
        // 1. boardRepository.findById(replyDTO.getBno()): boardRepository를 통해 replyDTO에 있는 bno를 사용하여 해당하는 Board 엔티티를 데이터베이스에서 찾습니다.
        // 2. orElseThrow(() -> new IllegalArgumentException("Invalid bno")): findById 메서드의 결과가 null이면 NoSuchElementException을 던집니다. 이를 방지하기 위해 orElseThrow 메서드를 사용하여 null이 아닌 경우에는 찾은 Board 엔티티를 반환하고, null인 경우에는 예외를 던집니다. 예외 메시지로는 "Invalid bno"를 사용합니다.
        // 3. reply.setBoard(board): 찾은 Board 엔티티를 사용하여 Reply 엔티티의 board 필드에 설정합니다. 이렇게 함으로써 Reply와 Board 간의 관계를 설정할 수 있습니다.
        Long rno = replyRepository.save(reply).getRno();
        log.info("ReplyServiceImpl register rno : " + rno);
        return rno;
    }

    @Override
    public ReplyDTO read(Long rno) {

        Optional<Reply> replyOptional = replyRepository.findById(rno);

        Reply reply = replyOptional.orElseThrow();

        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {

        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno());

        Reply reply = replyOptional.orElseThrow();

        reply.changeText(replyDTO.getReplyText());

        replyRepository.save(reply);

    }

    @Override
    public void remove(Long rno) {

        replyRepository.deleteById(rno);

    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {

        log.info("PageRequestDTO : " + pageRequestDTO);
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending());
        log.info("ReplyServiceImpl의 getListOfBoard의 Pageable : " + pageable);

        log.info("1.bno 값이 전달되었습니다." + "(bno=" + bno + ")");

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
        log.info("ReplyServiceImpl의 getListOfBoard의 result : " + result.getContent());

        List<ReplyDTO> dtoList =
                result.getContent().stream().map(reply -> modelMapper.map(reply, ReplyDTO.class))
                        .collect(Collectors.toList());

        log.info("2.bno 값이 전달되었습니다." + "(bno=" + bno + ")");

        log.info("ReplyServiceImpl의 getListOfBoard의 dtoList : " + dtoList);
        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

}
