package sharechat.com.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private String Id;

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

    public UserInfo(String id,String password,String workplace,String region,int age,String gender,String signature){
        this.Id=id;
        this.password=password;
        this.workplace=workplace;
        this.region=region;
        this.age=age;
        this.gender=gender;
        this.signature=signature;
    }
    public String getId(){
        return Id;
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
}
