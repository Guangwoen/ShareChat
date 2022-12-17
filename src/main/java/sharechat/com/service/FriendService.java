package sharechat.com.service;

import org.springframework.stereotype.Service;
import sharechat.com.entity.GroupNode;
import sharechat.com.entity.LinkNode;
import sharechat.com.repository.GroupNodeRepository;
import sharechat.com.repository.LinkNodeRepository;

import java.util.*;

@Service
public class FriendService {
    private final LinkNodeRepository linkNodeRepository;
    private final GroupNodeRepository groupNodeRepository;

    public FriendService(LinkNodeRepository linkNodeRepository,
                         GroupNodeRepository groupNodeRepository) {
        this.linkNodeRepository = linkNodeRepository;
        this.groupNodeRepository = groupNodeRepository;
    }

    /**
     * 插入用户节点
     * */
    public LinkNode save(LinkNode linkNode) {
        return linkNodeRepository.save(linkNode);
    }

    public void saveNewUser(LinkNode linkNode, GroupNode groupNode) {
        // 用户Id的重复性已经检查
        save(linkNode);
        if(!groupNodeRepository.existsGroupNodeByName(groupNode.getName())) {
            saveGroup(groupNode);
        }
        groupNodeRepository.linkWithLinkNode(groupNode.getName(),
                linkNode.getUserId());
    }

    public List<LinkNode> getRecommand(String userId) {
        List<LinkNode> recommand =  linkNodeRepository.getRecommandById(userId);
        List<LinkNode> returnLst = new ArrayList<>();
        for(LinkNode l: recommand) {
            if(!isLinked(userId, l.getUserId())) returnLst.add(l);
        }
        return returnLst;
    }

    /**
     * 插入所属团体节点
     */
    public GroupNode saveGroup(GroupNode groupNode) {
        return groupNodeRepository.save(groupNode);
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
        for (LinkNode l : nodeSet) {
            Map<String, Object> mapping = new HashMap<>();
            mapping.put("userId", l.getUserId());
            mapping.put("username", l.getName());
            mapping.put("flag", isLinked(id, l.getUserId()));
            returnLst.add(mapping);
        }
        return returnLst;
    }
}
