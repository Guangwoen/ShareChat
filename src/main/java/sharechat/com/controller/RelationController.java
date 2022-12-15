package sharechat.com.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sharechat.com.entity.User;
import sharechat.com.repository.FriendRelationRepository;
import sharechat.com.util.result.Result;

import java.util.List;

@RestController
@RequestMapping("/api/relation")
public class RelationController {

    private final FriendRelationRepository friendRelationRepository;

    public RelationController(FriendRelationRepository friendRelationRepository) {
        this.friendRelationRepository = friendRelationRepository;
    }

    @PostMapping("/new")
    public Result<List<User>> saveNewRelation(@RequestBody User u1,
                                              @RequestBody User u2) {
        return null;
    }
}
