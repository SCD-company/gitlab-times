package com.scd.gitlabtimeback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scd.gitlabtimeback.entity.Project;

@Repository
public interface ProjectRepository  extends JpaRepository<Project,Long>{
    
}
