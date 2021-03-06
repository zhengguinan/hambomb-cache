/*
 * Copyright 2019 The  Project
 *
 * The   Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.hambomb.examples;

import org.hambomb.cache.examples.entity.BPerson;
import org.hambomb.cache.examples.entity.Person;
import org.hambomb.cache.examples.entity.Phone;
import org.hambomb.cache.examples.service.ModifyPerson;
import org.hambomb.cache.examples.service.PhoneCond;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author: <a herf="mailto:jarodchao@126.com>jarod </a>
 * @date: 2019-04-02
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AutoConfigApplication.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class TestAutoConfig {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void test_getPersonByCardId() throws Exception {

        String cardId = "10000000000000005";

        this.webTestClient.get().uri("/hambomb/cardIds/{cardId}", cardId)
                .exchange().expectStatus().isOk().expectBody(String.class).value(s1 -> System.out.println(s1));
    }

    @Test
    public void test_getPersonById() throws Exception {

        Long id = 1L;

        this.webTestClient.get().uri("/hambomb/person/{id}", id)
                .exchange().expectStatus().isOk().expectBody(String.class).value(s1 -> System.out.println(s1));
    }

    @Test
    public void test_putPersonById() {
        Person person = new Person();

        person.setId(1L);
        person.setAddress("中国河北西伯坡");
        this.webTestClient.put().uri("/hambomb/persons").syncBody(person)
                .exchange().expectStatus().isOk()
                .expectBody(String.class).value(s -> System.out.println(s));
    }

    @Test
    public void test_putPerson() {
        ModifyPerson person = new ModifyPerson();

        person.setId(1L);
        person.setAddress("中国河北西伯坡");
        this.webTestClient.put().uri("/hambomb/persons/all").syncBody(person)
                .exchange().expectStatus().isOk()
                .expectBody(String.class).value(s -> System.out.println(s));
    }

    @Test
    public void test_PostPeron() {

        Person person = new Person();
        person.setName("JJ");
        person.setAddress("ddd");
        person.setAge(11);
        person.setCardId("11111111111111");
        person.setGender("nan");
        person.setHeight("111");
        person.setWeight(100.0);

        this.webTestClient.post().uri("/hambomb/persons").syncBody(person)
                .exchange().expectStatus().isOk()
                .expectBody(String.class).value(s -> System.out.println(s));

    }

    @Test
    public void test_PostBPerson() {

        BPerson person = new BPerson();
        person.setName("JJ");
        person.setAddress("ddd");
        person.setAge(11);
        person.setCardId("11111111111111");
        person.setGender("nan");
        person.setHeight("111");
        person.setWeight(100.0);

        this.webTestClient.post().uri("/hambomb/bpersons").syncBody(person)
                .exchange().expectStatus().isOk()
                .expectBody(String.class).value(s -> System.out.println(s));

    }

    @Test
    public void test_deletePerson(){
        this.webTestClient.delete().uri("/hambomb/persons/{id}", 2L)
                .exchange().expectStatus().isOk();
    }

    @Test
    public void test_getPhoneByCond(){

        PhoneCond cond = new PhoneCond("华为", "Mate 20", 16, "黑色");
        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/hambomb/phones")
                        .queryParam("brand", cond.getBrand())
                        .queryParam("model", cond.getModel())
                        .queryParam("memory", cond.getMemory())
                        .queryParam("color", cond.getColor()).build()).exchange().expectStatus().isOk();
    }

    @Test
    public void test_getPhoneByCond1(){

        PhoneCond cond = new PhoneCond("Apple", "IPhone 7", 127, "黑色");
        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/hambomb/phones")
                        .queryParam("brand", cond.getBrand())
                        .queryParam("model", cond.getModel())
                        .queryParam("memory", cond.getMemory())
                        .queryParam("color", cond.getColor()).build()).exchange().expectStatus().isOk();
    }

    @Test
    public void test_putPhoneByObject() {

        Phone phone = new Phone("华为", "Mate 20", 32, "黑色","拉萨");

        this.webTestClient.put().uri("/hambomb/phones").syncBody(phone)
                .exchange().expectStatus().isOk()
                .expectBody(String.class).value(s -> System.out.println(s));


    }

    @Test
    public void test_deletePhoneByObject() {

        PhoneCond cond = new PhoneCond("华为", "Mate 20", 16, "黑色");

        this.webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/hambomb/phones")
                .queryParam("brand", cond.getBrand())
                .queryParam("model", cond.getModel())
                .queryParam("memory", cond.getMemory())
                .queryParam("color", cond.getColor()).build()).exchange().expectStatus().isOk();


    }

    @Test
    public void test_deletePhoneByObject1() {

        PhoneCond cond = new PhoneCond("华为", "Mate 20", 16, "银色");

        this.webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/hambomb/phones")
                .pathSegment(cond.getBrand(),cond.getModel(),String.valueOf(cond.getMemory()),cond.getColor())
                .build()).exchange().expectStatus().isOk();


    }


}
