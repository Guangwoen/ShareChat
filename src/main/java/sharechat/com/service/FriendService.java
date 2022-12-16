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

    public boolean isLinked(String userId, String friendId) {
        Long linkCount = linkNodeRepository.isLinked(userId, friendId);
        System.out.println(userId+"_"+friendId);
        System.out.println(linkCount);
        return linkCount != 0;
    }

    /**
     * 根据Name获取所有朋友
     *
     * @param name 请求者Name
     */
    public List<LinkNode> getAllFriend(String name) {
        return linkNodeRepository.getFriendsByName(name);
    }

    public List<Map<String, ?>> searchUser(String id, String info) {
        List<LinkNode> nameList = linkNodeRepository.getLinkNodesByName(info);
        List<LinkNode> idList = linkNodeRepository.getLinkNodesByUserId(info);
        Set<LinkNode> nodeSet = new HashSet<>();
        nodeSet.addAll(nameList);
        nodeSet.addAll(idList);
        List<Map<String, ?>> returnLst = new ArrayList<>();
        for(LinkNode l: nodeSet) {
            Map<String, Object> mapping = new HashMap<>();
            mapping.put("userId", l.getUserId());
            mapping.put("username", l.getName());
            mapping.put("flag", isLinked(id, l.getUserId()));
            returnLst.add(mapping);
        }
        return returnLst;
    }

}
