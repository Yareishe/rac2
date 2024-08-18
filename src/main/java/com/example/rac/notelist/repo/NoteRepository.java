package com.example.rac.notelist.repo;


import com.example.rac.notelist.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {

}
