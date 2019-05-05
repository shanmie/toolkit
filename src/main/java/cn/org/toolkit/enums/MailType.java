package cn.org.toolkit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author deacon
 * @since 2019/5/5
 */
@Getter
@AllArgsConstructor
public enum MailType {
    simple(1),
    attachment(2),
    html(3),
    html_image(4),

    ;

    private int value;
}
