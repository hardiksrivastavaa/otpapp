package com.otpapp.otp.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.otpapp.otp.dto.ResponseDto;
import com.otpapp.otp.dto.StudentInfoDto;
import com.otpapp.otp.model.News;
import com.otpapp.otp.model.Qb;
import com.otpapp.otp.model.Response;
import com.otpapp.otp.model.Result;
import com.otpapp.otp.model.StudentInfo;
import com.otpapp.otp.services.NewsService;
import com.otpapp.otp.services.QbRepo;
import com.otpapp.otp.services.ResponseRepo;
import com.otpapp.otp.services.ResultRepo;
import com.otpapp.otp.services.StudentInfoRepo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    ResponseRepo resrepo;

    @Autowired
    StudentInfoRepo srepo;

    @Autowired
    QbRepo qbRepo;

    @Autowired
    ResultRepo resultRepo;

    @Autowired
    private NewsService newsService;

    // ____________________________________________________________________________________________________

    @GetMapping("/studenthome")
    public String showDashboard(HttpSession session, HttpServletResponse response, Model model) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("studentid") != null) {
                StudentInfo sInfo = srepo.findById(session.getAttribute("studentid").toString()).get();
                model.addAttribute("sInfo", sInfo);

                // Fetch all the latest news
                List<News> newsList = newsService.getAllNews(); // Use the method here
                model.addAttribute("newsList", newsList);

                StudentInfoDto dto = new StudentInfoDto();
                model.addAttribute("dto", dto);

                return "student/studenthome";
            } else {
                return "redirect:/studentlogin";
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return "redirect:/studentlogin";
        }
    }

    // __________________________________________________________________________________________________________________

    @PostMapping("/studenthome")
public String uploadPic(HttpSession session, RedirectAttributes redirectAttributes,
                        @ModelAttribute StudentInfoDto studentInfoDto) {
    if (session.getAttribute("studentid") != null) {
        try {
            MultipartFile fileData = studentInfoDto.getProfilepic();
            
            // Validate file type
            String contentType = fileData.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                redirectAttributes.addFlashAttribute("msg", "Invalid file type. Only image files (JPEG, PNG) are allowed.");
                return "redirect:/student/studenthome";
            }

            // Generate a unique file name and prepare for upload
            String storageFileName = new Date().getTime() + "_" + fileData.getOriginalFilename();
            String uploadDir = "public/user/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file
            try (InputStream inputStream = fileData.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                           StandardCopyOption.REPLACE_EXISTING);
            }

            // Update and save student profile
            StudentInfo studentInfo = srepo.findById(session.getAttribute("studentid").toString()).orElse(null);
            if (studentInfo != null) {
                studentInfo.setProfilepic(storageFileName);
                srepo.save(studentInfo);
            }

            redirectAttributes.addFlashAttribute("msg", "Profile uploaded successfully.");
            return "redirect:/student/studenthome";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg", "An error occurred while uploading the profile picture.");
            return "redirect:/student/studenthome";
        }
    } else {
        return "redirect:/studentlogin";
    }
}

    // ________________________________________________________________________________________________________

    @GetMapping("/changepassword")
    public String showChangePassword(HttpSession session, HttpServletResponse response) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("studentid") != null) {
                return "student/changepassword";
            } else {
                return "redirect:/studentlogin";
            }
        } catch (Exception e) {
            return "redirect:/studentlogin";
        }
    }

    // ________________________________________________________________________________________________________-

    @PostMapping("/changepassword")
    public String changePassword(HttpSession session, HttpServletResponse response, HttpServletRequest request,
            RedirectAttributes attrib) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("studentid") != null) {
                StudentInfo s = srepo.getById(session.getAttribute("studentid").toString());
                String oldpassword = request.getParameter("old_password");
                String newpassword = request.getParameter("new_password");
                String confirmpassword = request.getParameter("confirm_password");

                if (!newpassword.equals(confirmpassword)) {
                    attrib.addFlashAttribute("msg", "New Password and Confirm Password are not matched!");
                    return "redirect:/student/changepassword";
                }
                if (!oldpassword.equals(s.getPassword())) {
                    attrib.addFlashAttribute("msg", "Old Password is not matched!");
                    return "redirect:/student/changepassword";
                }
                s.setPassword(newpassword);
                srepo.save(s);
                return "redirect:/student/logout";
            } else {
                return "redirect:/studentlogin";
            }
        } catch (Exception e) {
            return "redirect:/studentlogin";
        }
    }

    // ___________________________________________________________________________________________________-

    @GetMapping("/trainingvideos")
    public String showTraningVidoes(HttpSession session, HttpServletResponse response) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("studentid") != null) {
                return "student/trainingvideos";
            } else {
                return "redirect:/studentlogin";
            }
        } catch (Exception e) {
            return "redirect:/studentlogin";
        }
    }

    // _____________________________________________________________________________________________________-

    @GetMapping("/viewresult")
    public String showResult(HttpSession session, HttpServletResponse response, Model model) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("studentid") != null) {

                // Check if result exists for the student
                Optional<Result> resultOptional = resultRepo.findById(session.getAttribute("studentid").toString());
                if (resultOptional.isPresent()) {
                    model.addAttribute("result", resultOptional.get());
                } else {
                    // If no result is found, show a message
                    model.addAttribute("resultMessage", "You haven't taken the test yet.");
                }

                return "student/viewresult";
            } else {
                return "redirect:/studentlogin";
            }
        } catch (Exception e) {
            return "redirect:/studentlogin";
        }
    }

    // ______________________________________________________________________________________________________

    @GetMapping("/response")
    public String showResponset(HttpSession session, HttpServletResponse response, Model model) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("studentid") != null) {
                ResponseDto dto = new ResponseDto();
                model.addAttribute("dto", dto);
                return "student/giveresponse";
            } else {
                return "redirect:/studentlogin";
            }
        } catch (Exception e) {
            return "redirect:/studentlogin";
        }
    }

    // ________________________________________________________________________________________________________-

    @PostMapping("/response")
    public String SubmitResponse(HttpSession session, Model model, @ModelAttribute ResponseDto responseDto,
            RedirectAttributes redirectAttributes, HttpServletResponse response) {
        try {
            if (session.getAttribute("studentid") != null) {
                StudentInfo std = srepo.getById(session.getAttribute("studentid").toString());
                model.addAttribute("studentid", session.getAttribute("userid"));

                Response res = new Response();

                res.setName(std.getName());
                res.setEnrollmentno(std.getEnrollmentno());
                res.setEmailaddress(std.getEmailaddress());
                res.setContactno(std.getContactno());
                res.setResponsetype(responseDto.getResponsetype());
                res.setSubject(responseDto.getSubject());
                res.setMessage(responseDto.getMessage());
                res.setResdate(new Date() + "");
                resrepo.save(res);

                redirectAttributes.addFlashAttribute("msg", "Your Response has been Submitted Successfully");
                return "redirect:/student/response";
            } else {
                return "redirect:/studentlogin";
            }

        } catch (Exception e) {
            return "redirect:/studentlogin";
        }

    }

    // ________________________________________________________________________________________________________-

    @GetMapping("/givetest")
    public String showTest(HttpSession session, HttpServletResponse response) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("studentid") != null) {
                return "student/givetest";
            } else {
                return "redirect:/studentlogin";
            }
        } catch (Exception e) {
            return "redirect:/studentlogin";
        }
    }

    // ________________________________________________________________________________________________________

    @GetMapping("/starttest")
    public String showStartTest(HttpSession session, HttpServletResponse response, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            if (session.getAttribute("studentid") != null) {
                StudentInfo sinfo = srepo.findById(session.getAttribute("studentid").toString()).get();
                model.addAttribute("sinfo", sinfo);
                String status = resultRepo.getStatus(sinfo.getEmailaddress());
                try {
                    if (status.equals("true")) {
                        redirectAttributes.addFlashAttribute("msg", "You have Already given the test");
                        return "redirect:/student/givetest";
                    } else {
                        String year = sinfo.getYear();
                        List<Qb> qList = qbRepo.findQbByYear(year);
                        Gson gson = new Gson();
                        String json = gson.toJson(qList);
                        model.addAttribute("json", json);
                        model.addAttribute("tt", qList.size() / 2);
                        model.addAttribute("tq", qList.size());
                        return "student/starttest";
                    }
                } catch (Exception e) {
                    String year = sinfo.getYear();
                    List<Qb> qList = qbRepo.findQbByYear(year);
                    Gson gson = new Gson();
                    String json = gson.toJson(qList);
                    model.addAttribute("json", json);
                    model.addAttribute("tt", qList.size() / 2);
                    model.addAttribute("tq", qList.size());
                    redirectAttributes.addFlashAttribute("msg", "something went wrong!");
                    return "student/starttest";
                }

            } else {
                return "redirect:/studentlogin";
            }
        } catch (Exception e) {
            return "redirect:/studentlogin";
        }
    }

    // ________________________________________________________________________________________________________

    @GetMapping("/testover")
    public String TestOver(HttpSession session, HttpServletResponse response, @RequestParam int s,
            @RequestParam int t) {
        try {
            response.setHeader("Cache-Control", "no-cache no-store, must-revalidate");
            if (session.getAttribute("studentid") != null) {
                StudentInfo si = srepo.getById(session.getAttribute("studentid").toString());
                Result rs = new Result();
                rs.setEmailaddress(si.getEmailaddress());
                rs.setName(si.getName());
                rs.setCourse(si.getCourse());
                rs.setCollegename(si.getCollegename());
                rs.setBranch(si.getBranch());
                rs.setContactno(si.getContactno());
                rs.setYear(si.getYear());
                rs.setTotalmarks(t);
                rs.setGetmarks(s);
                rs.setStatus("true");
                resultRepo.save(rs);
                return "student/testover";
            }
        } catch (Exception e) {
            return "redirect/studentlogin";
        }
        return "redirect/studentlogin";
    }

    // ________________________________________________________________________________________________________

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate session to log out the user
        return "redirect:/studentlogin"; // Redirect to the login page after logging o
    }

}
