package sharechat.com.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sharechat.com.entity.LinkNode;
import sharechat.com.entity.Message;
import sharechat.com.repository.LinkNodeRepository;

import java.util.*;

@Service
public class FriendService {
    private final LinkNodeRepository linkNodeRepository;

    public FriendService(LinkNodeRepository linkNodeRepository) {
        this.linkNodeRepository = linkNodeRepository;
    }

    public LinkNode save(LinkNode linkNode) {
        return linkNodeRepository.save(linkNode);
    }

    public String makeLink(String a, String b) {
        linkNodeRepository.makeLink(a, b);
        return a+"_"+b;
    }

    /**
     * 根据Name获取所有朋友
     *
     * @param name 请求者Name
     */
    public List<LinkNode> getAllFriend(String name) {
        return linkNodeRepository.getFriendsByName(name);
    }

    public List<LinkNode> searchUser(String info) {
        linkNodeRepository.getFriendsByName(info);
        return null;
    }

}
