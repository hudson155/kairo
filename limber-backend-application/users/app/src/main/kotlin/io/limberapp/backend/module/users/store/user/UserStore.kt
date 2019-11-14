package io.limberapp.backend.module.users.store.user

import io.limberapp.backend.module.users.model.user.UserModel
import io.limberapp.framework.store.Store

internal interface UserStore :
    Store<UserModel.Creation, UserModel.Complete, UserModel.Update>
