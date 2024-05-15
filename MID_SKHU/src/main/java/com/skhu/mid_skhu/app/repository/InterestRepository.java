package com.skhu.mid_skhu.app.repository;

import com.skhu.mid_skhu.app.entity.interest.Interest;
import com.skhu.mid_skhu.app.entity.interest.InterestCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    Optional<Interest> findAllByCategory(InterestCategory category);

    List<Interest> findAllByCategoryIn(List<InterestCategory> categories);

}
