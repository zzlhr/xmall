package com.lhrsite.xshop.service.impl;

import com.lhrsite.xshop.po.App;
import com.lhrsite.xshop.po.User;
import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.repository.AppRepository;
import com.lhrsite.xshop.service.AppService;
import com.lhrsite.xshop.service.UserService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;


@Service
public class AppServiceImpl implements AppService {
    private final UserService userService;

    private final AppRepository appRepository;

    @Autowired
    public AppServiceImpl(UserService userService,
                          AppRepository appRepository) {
        this.userService = userService;
        this.appRepository = appRepository;
    }

    @Override
    public String getPicture() {
        App app = appRepository.findById(1).orElse(new App());
        if (app.getPicture() == null) {
            app.setPicture(new JSONArray().toString());
        }
        return app.getPicture();
    }

    @Override
    public String getLink() {
        App app = appRepository.findAll().get(0);

        return app.getLink();
    }

    @Override
    public void edit(App app, String token) throws XShopException {
        User user = userService.tokenGetUser(token);
        if (user == null || user.getAdmin() != 1) {
            throw new XShopException(ErrEumn.ONLY_ADMIN_CAN_DO);
        }
        app.setId(1);
        appRepository.save(app);
    }
}
