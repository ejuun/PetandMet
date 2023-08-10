package com.ssafy.petandmet.repository;

import com.ssafy.petandmet.domain.Walk;
import com.ssafy.petandmet.dto.walk.UserWalkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WalkRepository extends JpaRepository<Walk, String> {

    @Query("select count(w) from Walk w " +
            "where w.user.uuid = :userUuid and w.animal.uuid = :animalUuid and w.status = com.ssafy.petandmet.domain.StatusType.PENDING")
    Long findCountByUserIdAndAnimalId(String userUuid, String animalUuid);

    @Query("select w.time from Walk w where w.date = :date and w.animal.uuid = :animalUuid and w.user.uuid = :userUuid")
    List<Integer> findWalkTimeByUserAndAnimal(String animalUuid, String userUuid, LocalDate date);

    @Query("select case when count(*) > 0 then true else false end from Walk w " +
            "where w.animal.uuid = :animalUuid and w.user.uuid = :userUuid and w.center.uuid = :centerUuid and w.date = :date and w.time = :time")
    boolean isExistWalkTime(String animalUuid, String userUuid, String centerUuid, LocalDate date, int time);

    @Query("select new com.ssafy.petandmet.dto.walk.UserWalkTime(w.date, w.time, w.status) from Walk w where w.user.uuid = :userUuid")
    List<UserWalkTime> getUserWalkTime(String userUuid);
}
