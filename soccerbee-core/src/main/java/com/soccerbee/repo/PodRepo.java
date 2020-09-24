package com.soccerbee.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Pod;

public interface PodRepo extends JpaRepository<Pod, String> {

}
