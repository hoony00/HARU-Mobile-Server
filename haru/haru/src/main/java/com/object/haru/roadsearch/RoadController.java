package com.object.haru.roadsearch;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoadController {

    @RequestMapping(value = "/rs")
    public ModelAndView RoadSearch(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("roadSearch");
        return mv;
    }

}
