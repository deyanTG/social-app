package uni.social.connect.validation;

import javax.validation.groups.Default;

public interface ValidationGroups {

    interface User {

        interface Update extends Default {
        }

        interface Create extends Default {
        }
    }


}
