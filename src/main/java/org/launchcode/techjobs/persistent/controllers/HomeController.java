package org.launchcode.techjobs.persistent.controllers;

import jakarta.validation.Valid;
import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    public EmployerRepository employerRepository;
    @Autowired
    public SkillRepository skillRepository;

    @Autowired
    public JobRepository jobRepository;

    public HomeController() {
    }

    @RequestMapping("/")
    public String index(Model model) {

        model.addAttribute("title", "MyJobs");
        List<Job> jobs = (List<Job>) jobRepository.findAll();
        model.addAttribute("jobs", jobs);
        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
	model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());
        List employers = (List<Employer>) employerRepository.findAll();
        model.addAttribute("employers", employers);
        List skills = (List<Skill>) skillRepository.findAll();
        model.addAttribute("skills", skills);
        return "add";
    }
// add code inside processAddJobForm method to select the employer object that has been
// chosen to be affiliated with the new job. You will need to select the
// employer using the request parameter you’ve added to the method.
    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                       Errors errors, Model model, @RequestParam int employerId, @RequestParam List<Integer> skills) {

        if (errors.hasErrors()) {
	    model.addAttribute("title", "Add Job");
        List employers = (List<Employer>) employerRepository.findAll();
        model.addAttribute("employers", employers);
            return "add";
        }
        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setSkills(skillObjs);
        jobRepository.save(newJob);
        return "redirect:./";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {
        Optional<Job> optJob = jobRepository.findById(jobId);
        if(optJob.isPresent()){
            Job job = optJob.get();
            model.addAttribute("job", job);
            return "view";
        }
         else {
            return "redirect:./";
        }
    }
}
