package hello.hellospring.controllerPractice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@Controller

public class HelloControllerPractice {
        @GetMapping("/") // template 경로를 기준으로 보는듯. , / 면 static으로 넘어가나?
        public String index() {
        return "index2"; // template 경로를 기준으로 어떤 주소로 갈 것인지 돌려주는듯 (templates/index2.html 로 간다.)
    }
    @GetMapping("question3") // 이 경로에 들어가면, 메소드 실행
    public String question33(Model model) { // 모델에 매개변수를 저장하는 메소드
        model.addAttribute("key", " = 저 는 값 이 에 요"); // setattribute 마냥 (속성, 값)
        return "question3"; // question3 경로로 값을 돌려준다.
    }

    @GetMapping("hello-man")
    public String helloMvc(@RequestParam(value="name", required = false) String name, Model model) {
            model.addAttribute("name", name);
            return "hello-template";
            // -mvc가 고유 명령어? => 아니다.

            // require : 입력값이 필요한가?
            // template/hello-man : require = false면 메소드 실행 true면 오류
            // template/hello-man?name=값 : require = true여도 실행

            // @RequestParam?
            // value?
            // name 끼리 상관관계 알아야함.
    }

}
