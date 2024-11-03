
package com.otpapp.otp.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.otpapp.otp.api.SmsSender;
import com.otpapp.otp.dto.AdminInfoDto;
import com.otpapp.otp.dto.EnquiryDto;
import com.otpapp.otp.dto.StudentInfoDto;
import com.otpapp.otp.model.AdminInfo;
import com.otpapp.otp.model.Enquiry;
import com.otpapp.otp.model.StudentInfo;
import com.otpapp.otp.services.AdminInfoRepo;
import com.otpapp.otp.services.EnquiryRepo;
import com.otpapp.otp.services.StudentInfoRepo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
    @Autowired
    EnquiryRepo erepo;

    @Autowired
    StudentInfoRepo sirepo;

    @Autowired
    AdminInfoRepo adrepo;

    // ___________________________________________________________________________________________________

    @GetMapping("/home")
    public String ShowIndex() {
        return "index";
    }

    @GetMapping("/experts")
    public String Experts() {
        return "experts";
    }

    @GetMapping("/aboutus")
    public String showAboutUs() {
        return "aboutus";
    }

    // ___________________________________________________________________________________________________

    @GetMapping("/studentlogin")
    public String showStudentLogin(Model model) {
        StudentInfoDto dto = new StudentInfoDto();
        model.addAttribute("dto", dto);
        return "studentlogin";
    }

    @PostMapping("/studentlogin")
    public String validateStudent(@ModelAttribute StudentInfoDto dto, HttpSession session, RedirectAttributes attrib) {
        try {
            StudentInfo s = sirepo.getById(dto.getEmailaddress());
            if (s.getPassword().equals(dto.getPassword())) {
                // attrib.addFlashAttribute("msg", "Valid User");
                session.setAttribute("studentid", s.getEmailaddress());
                return "redirect:/student/studenthome";
            } else {
                attrib.addFlashAttribute("msg", "Invalid User");
            }
            return "redirect:/studentlogin";

        } catch (EntityNotFoundException e) {
            attrib.addFlashAttribute("msg", "Student does'nt exists!");
            return "redirect:/studentlogin";
        }
    }

    // ___________________________________________________________________________________________________

    @GetMapping("/adminlogin")
    public String showAdminLogin(Model model) {
        AdminInfoDto dto = new AdminInfoDto();
        model.addAttribute("dto", dto);
        return "adminlogin";
    }

    @PostMapping("/adminlogin")
    public String validateAdmin(@ModelAttribute AdminInfoDto adminInfoDto, HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            AdminInfo adInfo = adrepo.getById(adminInfoDto.getUserid());
            if (adInfo.getPassword().equals(adminInfoDto.getPassword())) {
                // redirectAttributes.addFlashAttribute("msg", "Valid User");
                session.setAttribute("adminid", adminInfoDto.getUserid());
                return "redirect:/admin/adhome";
            } else {
                redirectAttributes.addFlashAttribute("msg", "Invalid User");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg", "Admin Does not exists");
            return "redirect:/adminlogin";
        }
        return "redirect:/adminlogin";
    }

    // ___________________________________________________________________________________________________

    @GetMapping("/register")
    public String showRegister(Model model) {
        StudentInfoDto sid = new StudentInfoDto();
        model.addAttribute("sid", sid);
        return "register";
    }

    @PostMapping("/register")
    public String submitRegister(@ModelAttribute StudentInfoDto studentInfoDto, BindingResult result,
            RedirectAttributes redirectAttributes) {
        try {
            StudentInfo si = new StudentInfo();
            si.setEnrollmentno(studentInfoDto.getEnrollmentno());
            si.setName(studentInfoDto.getName());
            si.setContactno(studentInfoDto.getContactno());
            si.setWhatsappno(studentInfoDto.getWhatsappno());
            si.setEmailaddress(studentInfoDto.getEmailaddress());
            si.setPassword(studentInfoDto.getPassword());
            si.setCollegename(studentInfoDto.getCollegename());
            si.setCourse(studentInfoDto.getCourse());
            si.setBranch(studentInfoDto.getBranch());
            si.setYear(studentInfoDto.getYear());
            si.setHighschool(studentInfoDto.getHighschool());
            si.setIntermediate(studentInfoDto.getIntermediate());
            si.setAggregatemarks(studentInfoDto.getAggregatemarks());
            si.setTrainingmode(studentInfoDto.getTrainingmode());
            si.setTraininglocation(studentInfoDto.getTraininglocation());
            si.setRegdate(new Date() + "");
            sirepo.save(si);
            redirectAttributes.addFlashAttribute("student", "Congratulations, Your Form has been Submitted Successfully!!");
            return "redirect:/register";

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("student", "Something went wrong!");
            return "redirect:/register";
        }

    }

    // ___________________________________________________________________________________________________

    @GetMapping("/contactus")
    public String ShowContactus(Model model) {
        EnquiryDto dto = new EnquiryDto();
        model.addAttribute("dto", dto);
        return "contactus";
    }

    @PostMapping("/contactus")
    public String SubmitEnquiry(@ModelAttribute EnquiryDto enquiryDto, BindingResult result,
            RedirectAttributes redirectAttributes) {

        try {
            Enquiry eq = new Enquiry();
            eq.setName(enquiryDto.getName());
            eq.setGender(enquiryDto.getGender());
            eq.setEnquiryText(enquiryDto.getEnquiryText());
            eq.setContactNo(enquiryDto.getContactNo());
            eq.setEmailAddress(enquiryDto.getEmailAddress());
            eq.setPostedDate(new Date() + "");
            erepo.save(eq);
            SmsSender ss = new SmsSender();
            ss.sendSms(enquiryDto.getContactNo());

            redirectAttributes.addFlashAttribute("message", "Form Submitted Successfully");
            return "redirect:/contactus";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went Wrong");
            return "redirect:/contactus";
        }
    }

    // ___________________________________________________________________________________________________

}
