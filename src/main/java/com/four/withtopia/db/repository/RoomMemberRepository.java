package com.four.withtopia.db.repository;

import com.four.withtopia.db.domain.Member;
import com.four.withtopia.db.domain.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {

    Long countAllBySessionId(String SessionId);


//    RoomMember findByMemberId(Long memberId);

    Optional<RoomMember> findBySessionIdAndNickname(String SessionId, String Nickname);
    List<RoomMember> findAllBySessionId(String sessionId);

    Optional<RoomMember> findByNickname(String nickName);
//    int countAllByRoomId(String roomId);



}
