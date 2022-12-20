package sharechat.com.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Table(name="userinfo")
@Data
@NoArgsConstructor
public class UserInfo {

    @Id
    @TableField("Id")
    private String id;

    @TableField("name")
    private String name;

    @TableField("password")
    private String password;

    @TableField("workplace")
    private String workplace;

    @TableField("region")
    private String region;

    @TableField("age")
    private int age;

    @TableField("gender")
    private String gender;

    @TableField("signature")
    private String signature;

    @TableField("headPicture")
    private String headPicture;

    @TableField("online")
    private boolean online;

    public UserInfo(String id,String name,String password,String workplace,String region,int age,String gender,String signature,String headPicture){
        this.id=id;
        this.name=name;
        this.password=password;
        this.workplace=workplace;
        this.region=region;
        this.age=age;
        this.gender=gender;
        this.signature=signature;
        this.headPicture=headPicture;
        this.online=false;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
    public String getWorkplace(){
        return workplace;
    }
    public String getRegion(){
        return region;
    }
    public int getAge(){
        return age;
    }
    public String getGender(){
        return gender;
    }
    public String getSignature(){
        return signature;
    }
    public boolean getOnline(){
        return online;
    }
    public String getHeadPicture(){
        return headPicture;
    }
}
