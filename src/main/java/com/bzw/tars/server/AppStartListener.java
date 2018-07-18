/**
 * Tencent is pleased to support the open source community by making Tars available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.bzw.tars.server;

import com.bzw.tars.client.kotlin.ClientImpl;
import com.qq.tars.server.core.AppContextEvent;
import com.qq.tars.server.core.AppContextListener;
import com.qq.tars.server.core.AppServantEvent;

public class AppStartListener implements AppContextListener {

    @Override
    public void appContextStarted(AppContextEvent event) {
    }

    @Override
    public void appServantStarted(AppServantEvent event) {
        System.out.println("==========appServantStarted==========");
//        ClientImpl.Companion.getInstance().loadConfig();
    }
}
