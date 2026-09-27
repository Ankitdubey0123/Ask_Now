package in.ankit.main.admin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardStatsDto {

    private long totalUsers;
    private long totalStudents;
    private long totalTeachers;
    private long totalAdmins;
    private long totalSessions;
    private long totalLiveSessions;
    private long totalCompletedSessions;
    private long totalReels;
    private long totalReelViews;
}
