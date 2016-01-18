package org.epnoi.modeler.builder;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.epnoi.storage.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 12/01/16.
 */
@Component
public class AuthorBuilder {


    //TODO this class will dissapear when create Users as entity domains
    public List<User> composeFromMetadata(String authoredBy){
        List <User> users = new ArrayList<>();

        for (String author : authoredBy.split(";")){
            User user = new User();
            user.setName(StringUtils.substringAfter(author,","));
            user.setSurname(StringUtils.substringBefore(author,","));
            user.setUri(composeUri(user.getName(),user.getSurname()));
            users.add(user);
        }



        return users;

    }

    private String composeUri(String name, String surname){
        try{
            return "http://epnoi.org/users/" + URIUtil.encodeQuery(name)+ "-" +URIUtil.encodeQuery(surname);
        }catch (Exception e){
            return "http://epnoi.org/users/default";
        }
    }

}
