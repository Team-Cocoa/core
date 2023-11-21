package kr.teamcocoa.core.test.network;

import kr.teamcocoa.core.network.controllers.mojang.ApiMojangController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("이름으로 UUID 검색하기")
class ApiMojangControllerTest {

    @DisplayName("올바른 UUID 검색하기")
    @Test
    void 이름으로_올바른_UUID_검색() {
        CompletableFuture<String> result = ApiMojangController.searchUUIDByName("fixca");
        try {
            String uuidString = result.get(10, TimeUnit.SECONDS);
            assertEquals("4c7f53d9-2eeb-425a-a261-4113705ebf64", uuidString);
        }
        catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
            fail();
        }
    }

    @DisplayName("없는 닉네임 검색하기")
    @Test
    void 없는_이름_검색() {
        CompletableFuture<String> result = ApiMojangController.searchUUIDByName("asdfwertgc");
        try {
            String uuidString = result.get(10, TimeUnit.SECONDS);
            assertNull(uuidString);
//            assertEquals("4c7f53d9-2eeb-425a-a261-4113705ebf64", uuidString);
        }
        catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
            fail();
        }
    }

}