/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package se.nbis.lega.inbox.filesystem;

import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.file.FileSystemFactory;
import org.apache.sshd.common.file.root.RootedFileSystemProvider;
import org.apache.sshd.common.session.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.Collections;

@Slf4j
@Service
public class LocalFileSystemFactory implements FileSystemFactory {

    private String inboxFolder;

    @Override
    public FileSystem createFileSystem(Session session) throws IOException {
        String username = session.getUsername();
        String root;
        if (inboxFolder.endsWith(File.separator)) {
            root = inboxFolder + username;
        } else {
            root = inboxFolder + File.separator + username;
        }
        File home = new File(root);
        home.mkdirs();
        log.info("Inbox initialized: {}", root);

        return new RootedFileSystemProvider().newFileSystem(home.toPath(), Collections.emptyMap());
    }

    @Value("${inbox.local.directory}")
    public void setInboxFolder(String inboxFolder) {
        this.inboxFolder = inboxFolder;
    }

}
