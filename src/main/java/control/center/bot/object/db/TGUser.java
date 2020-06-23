package control.center.bot.object.db;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class TGUser {


        @Id
        private Integer id;

        private String firstName;
        private Boolean isBot;
        private String lastName;
        private String userName;
        private Integer likes = 0;
        private Integer dislike = 0;
        private Integer rofl = 0;

}
