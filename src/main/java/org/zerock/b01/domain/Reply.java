package org.zerock.b01.domain;

import lombok.*;
import javax.persistence.*;

@Entity // 중복된 데이터 값을 비교할 수 있는 primary key가 필요하다
@Table(name = "Reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "board") // 상호 참조를 제외하는 방식, 단방향
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.EAGER)
    private Board board;
    private String replyText;
    private String replyer;

    public void changeText(String text) {
        this.replyText = text;
    }
}
