package com.otpapp.otp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.otpapp.otp.Repository.NewsRepository;
import com.otpapp.otp.dto.NewsDto;
import com.otpapp.otp.dto.QbDto;
import com.otpapp.otp.model.AdminInfo;
import com.otpapp.otp.model.Enquiry;
import com.otpapp.otp.model.News;
import com.otpapp.otp.model.Qb;
import com.otpapp.otp.model.Response;
import com.otpapp.otp.model.Result;
import com.otpapp.otp.model.StudentInfo;
import com.otpapp.otp.services.AdminInfoRepo;
import com.otpapp.otp.services.EnquiryRepo;
import com.otpapp.otp.services.NewsService;
import com.otpapp.otp.services.QbRepo;
import com.otpapp.otp.services.ResponseRepo;
import com.otpapp.otp.services.ResultRepo;
import com.otpapp.otp.services.StudentInfoRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    StudentInfoRepo stdrepo;

    @Autowired
    ResponseRepo resrepo;

    @Autowired
    EnquiryRepo eqrepo;

    @Autowired
    QbRepo qbRepo;

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    ResultRepo resultRepo;

    @Autowired
    AdminInfoRepo adminInfoRepo;

    @Autowired
    private NewsService newsService;

    @GetMapping("/adhome")
    public String ShowAdminHome(HttpSession session, HttpServletResponse response, Model model) {
        try {
            response.setHeader("cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("adminid") != null) {

                long stdcount = stdrepo.count();
                model.addAttribute("stdcount", stdcount);

                List<Response> clist = resrepo.FindResponseByResponesType("Complaint");
                long complaintcount = clist.size();
                model.addAttribute("complaintcount", complaintcount);

                List<Response> flist = resrepo.FindResponseByResponesType("Feedback");
                long feedbackcount = flist.size();
                model.addAttribute("feedbackcount", feedbackcount);

                long enquirycount = eqrepo.count();
                model.addAttribute("enquirycount", enquirycount);

                long qbcount = qbRepo.count();
                model.addAttribute("qbcount", qbcount);

                long resultcount = resultRepo.count();
                model.addAttribute("resultcount", resultcount);

                return "admin/adminhome";
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // ____________________________________________________________________________________________________

    @GetMapping("/viewstudents")
    public String ShowViewStudent(HttpSession session, HttpServletResponse response, Model model) {
        try {
            response.setHeader("cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("adminid") != null) {

                List<StudentInfo> slist = stdrepo.findAll();
                model.addAttribute("slist", slist);

                return "admin/viewstudents";
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // _____________________________________________________________________________________________________

    @GetMapping("/viewfeedback")
    public String ShowFeedback(HttpSession session, HttpServletResponse response, Model model) {
        try {
            response.setHeader("cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("adminid") != null) {

                List<Response> flist = resrepo.FindResponseByResponesType("Feedback");
                model.addAttribute("flist", flist);

                return "admin/viewfeedback";
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // ______________________________________________________________________________________________________

    @GetMapping("/viewcomplaint")
    public String ShowComplaint(HttpSession session, HttpServletResponse response, Model model) {
        try {
            response.setHeader("cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("adminid") != null) {

                List<Response> clist = resrepo.FindResponseByResponesType("Complaint");
                model.addAttribute("clist", clist);

                return "admin/viewcomplaint";
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // ______________________________________________________________________________________________________

    @GetMapping("/viewenquery")
    public String ShowEnquery(HttpSession session, HttpServletResponse response, Model model) {
        try {
            response.setHeader("cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("adminid") != null) {

                List<Enquiry> eqlist = eqrepo.findAll();
                model.addAttribute("eqlist", eqlist);

                return "admin/viewenquery";
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // ______________________________________________________________________________________________________

    @GetMapping("/viewstudents/delete")
    public String deleteButton(@RequestParam String email, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            if (session.getAttribute("adminid") != null) {
                StudentInfo st = stdrepo.findById(email).get();
                stdrepo.delete(st);
                redirectAttributes.addFlashAttribute("msg", email + " is deleted successfully!");
                return "redirect:/admin/viewstudents";
            } else {
                return "redirect:/adminlogin";

            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // ______________________________________________________________________________________________________

    @GetMapping("/viewfeedback/delete")
    public String deletefeedbackButton(@RequestParam Integer resid, HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            if (session.getAttribute("adminid") != null) {
                Response fd = resrepo.findById(resid).get();
                resrepo.delete(fd);
                redirectAttributes.addFlashAttribute("msg", resid + "Feedback is deleted successfully!");
                return "redirect:/admin/viewfeedback";
            } else {
                return "redirect:/adminlogin";

            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }
    // ________________________________________________________________________________________

    @GetMapping("/viewcomplaint/delete")
    public String deletecomplaintButton(@RequestParam Integer resid, HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            if (session.getAttribute("adminid") != null) {
                Response cd = resrepo.findById(resid).get();
                resrepo.delete(cd);
                redirectAttributes.addFlashAttribute("msg", "Complaint is deleted successfully!");
                return "redirect:/admin/viewcomplaint";
            } else {
                return "redirect:/adminlogin";

            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // ________________________________________________________________________________________

    @GetMapping("/addqb")
    public String AddQb(HttpSession session, HttpServletResponse response, Model model) {
        try {
            response.setHeader("cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("adminid") != null) {
                QbDto dto = new QbDto();
                model.addAttribute("dto", dto);
                return "admin/addqb";
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // ________________________________________________________________________________________

    @PostMapping("/addqb")
    public String CreateQb(HttpSession session, HttpServletResponse response, @ModelAttribute QbDto dto,
            RedirectAttributes attrib) {
        try {
            response.setHeader("cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("adminid") != null) {
                Qb qb = new Qb();
                qb.setYear(dto.getYear());
                qb.setQuestion(dto.getQuestion());
                qb.setA(dto.getA());
                qb.setB(dto.getB());
                qb.setC(dto.getC());
                qb.setD(dto.getD());
                qb.setCorrect(dto.getCorrect());
                qbRepo.save(qb);
                attrib.addFlashAttribute("msg", "Question is Added!!");
                return "redirect:/admin/addqb";
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // ______________________________________________________________________________________

    @GetMapping("/viewqb")
    public String ViewQb(HttpSession session, HttpServletResponse response, Model model) {
        try {
            response.setHeader("cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("adminid") != null) {

                List<Qb> rList = qbRepo.findAll();
                model.addAttribute("rList", rList);

                return "admin/viewqb";
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // _____________________________________________________________________________________________________

    @GetMapping("/viewqb/edit")
    public String showEditForm(@RequestParam Integer id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            if (session.getAttribute("adminid") != null) {
                Qb qb = qbRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid question ID: " + id));
                model.addAttribute("qb", qb);
                return "admin/editqb"; // Corrected path to the edit form
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Error occurred while editing the question.");
            return "redirect:/admin/viewqb";
        }
    }

    // ___________________________________________________________________________________________________

    @PostMapping("/viewqb/edit")
    public String updateQuestion(@ModelAttribute("qb") Qb updatedQb, HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            if (session.getAttribute("adminid") != null) {
                // Fetch the existing question by ID
                Qb existingQb = qbRepo.findById(updatedQb.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid question ID: " + updatedQb.getId()));

                // Update the fields
                existingQb.setYear(updatedQb.getYear());
                existingQb.setQuestion(updatedQb.getQuestion());
                existingQb.setA(updatedQb.getA());
                existingQb.setB(updatedQb.getB());
                existingQb.setC(updatedQb.getC());
                existingQb.setD(updatedQb.getD());
                existingQb.setCorrect(updatedQb.getCorrect());

                // Save the updated question back to the database
                qbRepo.save(existingQb);

                redirectAttributes.addFlashAttribute("msg", "Question updated successfully!");
                return "redirect:/admin/viewqb"; // Redirect back to question list page
            } else {
                return "redirect:/adminlogin"; // Redirect to login if session expired
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Error occurred while updating the question.");
            return "redirect:/admin/viewqb";
        }
    }

    // _____________________________________________________________________________________________________

    @GetMapping("/viewqb/delete")
    public String deleteQbButton(@RequestParam Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            if (session.getAttribute("adminid") != null) {
                Qb qb = qbRepo.findById(id).get();
                qbRepo.delete(qb);
                redirectAttributes.addFlashAttribute("msg", "Question is deleted successfully!");
                return "redirect:/admin/viewqb";
            } else {
                return "redirect:/adminlogin";

            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // ________________________________________________________________________________________

    @GetMapping("/viewenquery/delete")
    public String deleteEnquiryButton(@RequestParam Integer id, HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            if (session.getAttribute("adminid") != null) {
                Enquiry eq = eqrepo.findById(id).get();
                eqrepo.delete(eq);
                redirectAttributes.addFlashAttribute("msg", "Enquiry is deleted successfully!");
                return "redirect:/admin/viewenquery";
            } else {
                return "redirect:/adminlogin";

            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // _______________________________________________________________________________________

    @GetMapping("/changepassword")
    public String showChangePassword(HttpSession session, HttpServletResponse response) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("adminid") != null) {
                return "admin/changepassword";
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception e) {
            return "redirect:/adminlogin";
        }
    }

    // _______________________________________________________________________________________

    @PostMapping("/changepassword")
    public String changePassword(HttpSession session, HttpServletResponse response, HttpServletRequest request,
            RedirectAttributes attrib) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("adminid") != null) {
                AdminInfo adminInfo = adminInfoRepo.getById(session.getAttribute("adminid").toString());
                String oldpassword = request.getParameter("old_password");
                String newpassword = request.getParameter("new_password");
                String confirmpassword = request.getParameter("confirm_password");

                if (!newpassword.equals(confirmpassword)) {
                    attrib.addFlashAttribute("msg", "New Password and Confirm Password are not matched!");
                    return "redirect:/admin/changepassword";
                }
                if (!oldpassword.equals(adminInfo.getPassword())) {
                    attrib.addFlashAttribute("msg", "Old Password is not matched!");
                    return "redirect:/admin/changepassword";
                }
                adminInfo.setPassword(newpassword);
                adminInfoRepo.save(adminInfo);
                return "redirect:/admin/logout";
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception e) {
            return "redirect:/adminlogin";
        }
    }

    // _______________________________________________________________________________________

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate session to log out the user
        return "redirect:/adminlogin"; // Redirect to the login page after logging o
    }

    // ________________________________________________________________________________________

    @GetMapping("/manageresult")
    public String manageresult(HttpSession session, HttpServletResponse response, Model model) {
        try {
            response.setHeader("cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("adminid") != null) {
                List<Result> rList = resultRepo.findAll();
                model.addAttribute("rList", rList);

                return "admin/manageresult";
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // ________________________________________________________________________________________

    @GetMapping("/manageresult/delete")
    public String deleteResult(@RequestParam String email, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            if (session.getAttribute("adminid") != null) {
                Result result = resultRepo.findById(email).get();
                resultRepo.delete(result);
                redirectAttributes.addFlashAttribute("msg", email + " is deleted successfully!");
                return "redirect:/admin/manageresult";
            } else {
                return "redirect:/adminlogin";

            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // ________________________________________________________________________________________

    // Show the manage news page to admin
    @GetMapping("/addnews")
    public String showManageNewsPage(Model model) {
        model.addAttribute("newsDto", new NewsDto());
        return "admin/addnews"; // Points to the admin news management view
    }

    // Handle the form submission for adding news
    @PostMapping("/addnews")
    public String addNews(@ModelAttribute NewsDto newsDto, Model model, RedirectAttributes redirectAttributes) {
        newsService.saveNews(newsDto);
        // model.addAttribute("successMessage", "News added successfully!");
        redirectAttributes.addFlashAttribute("successMessage", "News added successfully!");

        // Redirect to the manage news page after submission
        return "redirect:/admin/addnews";
    }

    // ______________________________________________________________________________________

    @GetMapping("/managenews")
    public String manageResult(HttpSession session, HttpServletResponse response, Model model) {
        try {
            response.setHeader("cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("adminid") != null) {

                List<News> nlist = newsRepository.findAll();
                model.addAttribute("nlist", nlist);

                return "admin/managenews";
            } else {
                return "redirect:/adminlogin";
            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

    // _____________________________________________________________________________________________________

    @GetMapping("/managenews/delete")
    public String deleteNews(@RequestParam Long resid, HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            if (session.getAttribute("adminid") != null) {
                News news = newsRepository.findById(resid).get();
                newsRepository.delete(news);
                redirectAttributes.addFlashAttribute("msg", "News is deleted successfully!");
                return "redirect:/admin/managenews";
            } else {
                return "redirect:/adminlogin";

            }
        } catch (Exception ex) {
            return "redirect:/adminlogin";
        }
    }

}
