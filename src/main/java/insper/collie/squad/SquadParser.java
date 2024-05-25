package insper.collie.squad;

import insper.collie.account.AccountOut;
import insper.collie.company.CompanyInfo;

public class SquadParser {

    public static Squad to(SquadInfo in) {
        return Squad.builder()
            .id(in.id())
            .name(in.name())
            .description(in.description())
            .company_id(in.company_id())
            .manager_id(in.manager_id())
            .build();
    }

    public static SquadInfo to(Squad in) {
        return SquadInfo.builder()
            .id(in.id())
            .name(in.name())
            .description(in.description())
            .company_id(in.company_id())
            .manager_id(in.manager_id())
            .build();
    }


    public static SquadAllInfo toAll(Squad in, CompanyInfo c, AccountOut a) {
        return SquadAllInfo.builder()
            .id(in.id())
            .name(in.name())
            .description(in.description())
            .company_id(in.company_id())
            .company_name(c.name())
            .company_description(c.description())
            .manager_id(in.manager_id())
            .manager_name(a.name())
            .build();
    }
}
