/*
 * Copyright (C) 2023-2023 The Gongliangyi Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jack.criminalintent.database;

/**
 * @author gongliangyi 1465306392@qq.com
 * @since 2023-02-24 12:43
 */
public class CrimeDbSchema {
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
           public static final String UUID = "uuid";
           public static final String TITLE = "title";
           public static final String DATE = "date";
           public static final String SOLVED = "solved";
           public static final String SUSPECT = "suspect";
        }
    }
}
