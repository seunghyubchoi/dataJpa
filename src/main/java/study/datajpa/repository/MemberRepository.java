package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    // 쿼리 어노테이션
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // @Query로 값 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // @Query로 DTO 조회
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name)from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    // 여러건 없으면 0
    List<Member> findListByUsername(String username);

    // 단건 없으면 null
    Member findMemberByUsername(String username);

    Optional<Member> findOptionalMemberByUsername(String username);

    // 카운트 쿼리 단순화
    @Query(value = "select m from Member m left join m.team t",
    countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    List<Member> findTop3ByAge(int age);

    // 벌크 연산
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
