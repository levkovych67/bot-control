package control.center.bot.service;


import control.center.bot.object.db.TGUser;
import control.center.bot.repo.TGUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TGUserService {


    @Autowired
    private TGUserDao tgUserDao;

    private TGUser create(User user){

        TGUser tgUser = new TGUser();
        tgUser.setFirstName(user.getFirstName());
        tgUser.setId(user.getId());
        tgUser.setLastName(user.getLastName());
        tgUser.setUserName(user.getUserName());

        System.out.println(user);
        return tgUserDao.save(tgUser);
    }


    private TGUser getOrCreate(User user){
        Optional<TGUser> byId = tgUserDao.findById(user.getId());
        return byId.orElseGet(() -> create(user));
     }

     private void like(User user){
         TGUser orCreate = getOrCreate(user);
         Integer like = orCreate.getLikes();
         orCreate.setLikes(like + 1);
         tgUserDao.save(orCreate);

     }

    private void dislike(User user){
        TGUser orCreate = getOrCreate(user);
        Integer dislike = orCreate.getDislike();
        orCreate.setDislike(dislike + 1);
        tgUserDao.save(orCreate);

    }

    private void rofl(User user){
        TGUser orCreate = getOrCreate(user);
        Integer rofl = orCreate.getRofl();
        orCreate.setRofl(rofl + 1);
        tgUserDao.save(orCreate);
    }

    public void  processReaction(String reaction, User user){
        if(reaction.equals("like")){
            like(user);
        }
        if(reaction.equals("rofl")){
            rofl(user);
        }
        if(reaction.equals("dislike")){
            dislike(user);
        }
    }


    public List<TGUser> getTop10(){
        return   tgUserDao
                .findAll()
                .stream()
                .filter(e -> e.getFirstName() != null)
                .sorted(Comparator.comparing(this::getReactions).reversed())
                .limit(10L).collect(Collectors.toList());
    }

    private Integer getReactions(TGUser tgUser){
        return tgUser.getDislike() + tgUser.getLikes() + tgUser.getRofl();
    }
}
