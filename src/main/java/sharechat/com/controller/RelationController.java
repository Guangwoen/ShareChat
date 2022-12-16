package sharechat.com.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sharechat.com.entity.LinkNode;
import sharechat.com.repository.LinkNodeRepository;
import sharechat.com.util.result.Result;

import java.util.List;

@RestController
@RequestMapping("/api/relation")
public class RelationController {

    private final LinkNodeRepository linkNodeRepository;

    public RelationController(LinkNodeRepository linkNodeRepository) {
        this.linkNodeRepository = linkNodeRepository;
    }

    @PostMapping("/new")
    public Result<List<LinkNode>> saveNewRelation(@RequestBody LinkNode u1,
                                                  @RequestBody LinkNode u2) {
        return null;
    }
}
