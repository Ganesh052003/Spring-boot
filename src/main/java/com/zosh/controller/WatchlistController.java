package com.zosh.controller;

import com.zosh.exception.UserException;
import com.zosh.model.Coin;
import com.zosh.model.Appuser;
import com.zosh.model.Watchlist;
import com.zosh.service.CoinService;
import com.zosh.service.UserService;
import com.zosh.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {
        private final WatchlistService watchlistService;
        private final UserService userService;

        @Autowired
        private CoinService coinService;

        @Autowired
        public WatchlistController(WatchlistService watchlistService,
                        UserService userService) {
                this.watchlistService = watchlistService;
                this.userService = userService;
        }

        @GetMapping("/appuser")
        public ResponseEntity<Watchlist> getUserWatchlist(
                        @RequestHeader("Authorization") String jwt) throws Exception {

                Appuser appuser = userService.findUserProfileByJwt(jwt);
                Watchlist watchlist = watchlistService.findUserWatchlist(appuser.getId());
                return ResponseEntity.ok(watchlist);

        }

        @PostMapping("/create")
        public ResponseEntity<Watchlist> createWatchlist(
                        @RequestHeader("Authorization") String jwt) throws UserException {
                Appuser appuser = userService.findUserProfileByJwt(jwt);
                Watchlist createdWatchlist = watchlistService.createWatchList(appuser);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdWatchlist);
        }

        @GetMapping("/{watchlistId}")
        public ResponseEntity<Watchlist> getWatchlistById(
                        @PathVariable Long watchlistId) throws Exception {

                Watchlist watchlist = watchlistService.findById(watchlistId);
                return ResponseEntity.ok(watchlist);

        }

        @PatchMapping("/add/coin/{coinId}")
        public ResponseEntity<Coin> addItemToWatchlist(
                        @RequestHeader("Authorization") String jwt,
                        @PathVariable String coinId) throws Exception {

                Appuser appuser = userService.findUserProfileByJwt(jwt);
                Coin coin = coinService.findById(coinId);
                Coin addedCoin = watchlistService.addItemToWatchlist(coin, appuser);
                return ResponseEntity.ok(addedCoin);

        }
}
