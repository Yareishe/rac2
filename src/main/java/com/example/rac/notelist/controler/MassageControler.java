package com.example.rac.notelist.controler;



import com.example.rac.notelist.entity.Note;
import com.example.rac.notelist.repo.UserRepository;
import com.example.rac.notelist.serves.NoteServicelmpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller()
@RequestMapping("/note")
@Slf4j
public class MassageControler {


    private NoteServicelmpl noteServicelmpl;

    private UserRepository userRepository;

    public MassageControler(NoteServicelmpl noteServicelmpl) {
        this.noteServicelmpl = noteServicelmpl;
    }


    @GetMapping("/list")
    public String listNotes(Model model) {
        model.addAttribute("notes", noteServicelmpl.listAll());
        return "list";
    }

    @PostMapping(value = "/delete")
    public String deleteNotes(@RequestParam Long id) {
        noteServicelmpl.deleteById(id);
        return "redirect:/note/list";
    }

    @GetMapping("/add")
    public String addgetNotes(@ModelAttribute("note") Note note, Model model) {
        return "add";
    }

    @PostMapping("/add")
    public String addpostNotes(@ModelAttribute("note") Note note, Model model) {
        noteServicelmpl.add(note);
        return "redirect:/note/list";
    }

    @GetMapping("/edit")
    public String editeNotes(@RequestParam("id") Long id, Model model) {
        Optional<Note> newNote1 = noteServicelmpl.getById(id);
        model.addAttribute("note", newNote1);
        return "edit";
    }

    @PostMapping("/edit")
    public String editNotes(@ModelAttribute("note") Note note, Model model) {

        noteServicelmpl.update(note);

        model.addAttribute("note", note);
        return "redirect:/note/list";
    }




}

