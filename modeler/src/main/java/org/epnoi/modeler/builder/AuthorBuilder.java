package org.epnoi.modeler.builder;

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
            user.setUri("temporal");
            user.setName(StringUtils.substringAfter(author,","));
            user.setSurname(StringUtils.substringBefore(author,","));
            users.add(user);
        }

        return users;

    }
}
