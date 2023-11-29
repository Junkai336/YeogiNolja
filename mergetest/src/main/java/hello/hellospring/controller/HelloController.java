package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller

public class HelloController {

//    @GetMapping("/")
//    public String index() {
//        return "index";
//    }
    @GetMapping("hellohi")
    public String hello(Model model) {
        model.addAttribute("data", "강경신님");
        return "hello";
    }

    @GetMapping("/fruit")
    public String getFruit(Model model) {
        // Model 객체를 통해 view(타임리프 html)로 전달
        Map<String, String> fruitmap = new HashMap<String, String>();
        fruitmap.put("fruit1", "apple");
        fruitmap.put("fruit2", "banana");
        fruitmap.put("fruit3", "orange");
        model.addAttribute("fruit", fruitmap);

        return "fruit/fruit";
    }
    // @GetMapping("/fruit")
    // /fruit 경로로 get 요청을 처리합니다.
    // Model model 이 파라미터를 통해 뷰로 전달
    // model.addAttribute("fruit", fruitmap);
    // fruitmap을 "fruit"라는 이름으로 모델에 추가합니다.
    // 이렇게 하면 "fruit"이라는 이름으로 뷰에서 이 데이터에 접근할 수 있게 됩니다.
    // return "fruit/fruit": 처리가 완료된 후,
    // "fruit/fruit" 뷰 템플릿을 반환합니다.
    // 이 뷰는 "/resources/templates" 디렉토리 또는 미리 설정된
    // 뷰 리졸버에 따라 렌더링 됩니다.

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam(value = "name", required = false) String name,Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    // @RequestParam("name") String name,
    // 요청 매개변수 중 name 값을 받아 들인다.
    // 예를 들어
    // hello-mvc?name=John와 같은 요청에서 "John" 값을 받습니다.


    // 단순 api 방식 - string 값 가지고 오기

    @GetMapping("hello-string")
    @ResponseBody // http 헤더부와 바디부가 있는데 바디부에 이 데이터를 직접 넣어줌
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }
}
