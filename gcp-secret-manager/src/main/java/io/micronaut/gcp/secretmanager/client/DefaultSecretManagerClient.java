/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.gcp.secretmanager.client;

import com.google.cloud.secretmanager.v1.*;
import io.micronaut.context.annotation.BootstrapContextCompatible;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.gcp.GoogleCloudConfiguration;
import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * Default implementation of {@link SecretManagerClient}.
 * @author Vinicius Carvalho
 * @since 3.4.0
 */
@Singleton
@BootstrapContextCompatible
@Requires(classes = SecretManagerServiceClient.class)
public class DefaultSecretManagerClient implements SecretManagerClient {

    private final SecretManagerServiceClient client;
    private final Environment environment;
    private final GoogleCloudConfiguration googleCloudConfiguration;
    private final Logger logger = LoggerFactory.getLogger(SecretManagerClient.class);

    public DefaultSecretManagerClient(SecretManagerServiceClient client, Environment environment, GoogleCloudConfiguration googleCloudConfiguration) {
        this.client = client;
        this.environment = environment;
        this.googleCloudConfiguration = googleCloudConfiguration;
    }

    @Override
    public Maybe<VersionedSecret> getSecret(String secretId) {
        return getSecret(secretId, LATEST , googleCloudConfiguration.getProjectId());
    }

    @Override
    public Maybe<VersionedSecret> getSecret(String secretId, String version) {
        return getSecret(secretId, version, googleCloudConfiguration.getProjectId());
    }

    @Override
    public Maybe<VersionedSecret> getSecret(String secretId, String version, String projectId) {
        logger.debug(String.format("Fetching secret: projects/%s/secrets/%s/%s", projectId, secretId, version));
        SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, version);
        AccessSecretVersionRequest request = AccessSecretVersionRequest.newBuilder()
                .setName(secretVersionName.toString())
                .build();
        return Maybe.fromFuture(client.accessSecretVersionCallable().futureCall(request))
                .map(response -> new VersionedSecret(secretId, projectId, version, response.getPayload().getData().toByteArray()))
                .onErrorResumeNext(Maybe.empty());
    }
}
