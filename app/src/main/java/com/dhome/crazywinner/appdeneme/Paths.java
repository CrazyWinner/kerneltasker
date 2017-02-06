package com.dhome.crazywinner.appdeneme;

/**
 * Created by Mertcan on 4.04.2015.
 */
interface Paths {
    String CPU_CUR_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_cur_freq";
    String CPUS_RELATED = "/sys/devices/system/cpu/cpu%d/cpufreq/related_cpus";
    String CPU_CORE_ONLINE = "/sys/devices/system/cpu/cpu%d/online";
    String CPU_MAX_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_max_freq";
    String CPU_RELATED="/sys/devices/system/cpu/cpu0/cpufreq/related_cpus";
    String CPU_MIN_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_min_freq";
    String CPU_MAX_SCREEN_OFF_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/screen_off_max_freq";
    String CPU_MSM_CPUFREQ_LIMIT = "/sys/kernel/msm_cpufreq_limit/cpufreq_limit";
    String CPU_AVAILABLE_FREQS = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_available_frequencies";
    String CPU_TIME_STATE = "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
    String CPU_SCALING_GOVERNOR = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_governor";
    String CPU_AVAILABLE_GOVERNORS = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_available_governors";
    String CPU_GOVERNOR_TUNABLES = "/sys/devices/system/cpu/cpufreq";
    String CPU_MC_POWER_SAVING = "/sys/devices/system/cpu/sched_mc_power_savings";
    String CPU_AVAILABLE_CFS_SCHEDULERS = "/sys/devices/system/cpu/sched_balance_policy/available_sched_balance_policy";
    String CPU_CURRENT_CFS_SCHEDULER = "/sys/devices/system/cpu/sched_balance_policy/current_sched_balance_policy";
    String IO_INTERNAL_SCHEDULER = "/sys/block/mmcblk0/queue/scheduler";
    String TCP_AVAILABLE_CONGESTIONS = "/proc/sys/net/ipv4/tcp_available_congestion_control";
    String TCP_CONGESTION="/proc/sys/net/ipv4/tcp_congestion_control";
    String CPU_VOLTAGE = "/sys/devices/system/cpu/cpu%d/cpufreq/UV_mV_table";
    String GPU_OVERCLOCK = "/sys/devices/system/cpu/cpu0/cpufreq/gpu_oc";
    String GPU_VOLTAGE="/sys/devices/system/cpu/cpu0/cpufreq/avp_UV_mV_table";
    String RAM_VOLTAGE="/sys/devices/system/cpu/cpu0/cpufreq/emc_UV_mV_table";
    String LP_VOLTAGE="/sys/devices/system/cpu/cpu0/cpufreq/lp_UV_mV_table";
    String CPU_FAUX_VOLTAGE = "/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels";
    String DYNAMIC_FSYNC = "/sys/kernel/dyn_fsync/Dyn_fsync_active";
    String FORCE_FAST_CHARGE = "/sys/kernel/fast_charge/force_fast_charge";
    String IO_EXTERNAL_SCHEDULER = "/sys/block/mmcblk1/queue/scheduler";
    String IO_INTERNAL_SCHEDULER_TUNABLE = "/sys/block/mmcblk0/queue/iosched";
    String IO_EXTERNAL_SCHEDULER_TUNABLE = "/sys/block/mmcblk1/queue/iosched";
    String IO_INTERNAL_READ_AHEAD = "/sys/block/mmcblk0/queue/read_ahead_kb";
    String IO_EXTERNAL_READ_AHEAD = "/sys/block/mmcblk1/queue/read_ahead_kb";

    String GPU_MAX_KGSL2D0_QCOM_FREQ = "/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk";
    String GPU_AVAILABLE_KGSL2D0_QCOM_FREQS = "/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpu_available_frequencies";
    String GPU_SCALING_KGSL2D0_QCOM_GOVERNOR = "/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/pwrscale/trustzone/governor";


    String GPU_MAX_KGSL3D0_QCOM_FREQ = "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk";
    String GPU_AVAILABLE_KGSL3D0_QCOM_FREQS = "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpu_available_frequencies";
    String GPU_SCALING_KGSL3D0_QCOM_GOVERNOR = "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/pwrscale/trustzone/governor";


    String GPU_MAX_FDB00000_QCOM_FREQ = "/sys/devices/fdb00000.qcom,kgsl-3d0/kgsl/kgsl-3d0/max_gpuclk";
    String GPU_AVAILABLE_FDB00000_QCOM_FREQS = "/sys/devices/fdb00000.qcom,kgsl-3d0/kgsl/kgsl-3d0/gpu_available_frequencies";
    String GPU_SCALING_FDB00000_QCOM_GOVERNOR = "/sys/devices/fdb00000.qcom,kgsl-3d0/kgsl/kgsl-3d0/devfreq/governor";
    String GPU_AVAILABLE_FDB00000_QCOM_GOVERNORS = "/sys/devices/fdb00000.qcom,kgsl-3d0/kgsl/kgsl-3d0/devfreq/available_governors";


    String GPU_MAX_1C00000_QCOM_FREQ = "/sys/devices/soc.0/1c00000.qcom,kgsl-3d0/kgsl/kgsl-3d0/max_gpuclk";
    String GPU_AVAILABLE_1C00000_QCOM_FREQ = "/sys/devices/soc.0/1c00000.qcom,kgsl-3d0/kgsl/kgsl-3d0/gpu_available_frequencies";
    String GPU_SCALING_1C00000_QCOM_GOVERNOR = "/sys/devices/soc.0/1c00000.qcom,kgsl-3d0/devfreq/1c00000.qcom,kgsl-3d0/governor";
    String GPU_AVAILABLE_1C00000_QCOM_GOVERNORS = "/sys/devices/soc.0/1c00000.qcom,kgsl-3d0/devfreq/1c00000.qcom,kgsl-3d0/available_governors";

    String GPU_MAX_OMAP_FREQ = "/sys/devices/platform/omap/pvrsrvkm.0/sgxfreq/frequency_limit";
    String GPU_AVAILABLE_OMAP_FREQS = "/sys/devices/platform/omap/pvrsrvkm.0/sgxfreq/frequency_list";
    String GPU_SCALING_OMAP_GOVERNOR = "/sys/devices/platform/omap/pvrsrvkm.0/sgxfreq/governor";
    String GPU_AVAILABLE_OMAP_GOVERNORS = "/sys/devices/platform/omap/pvrsrvkm.0/sgxfreq/governor_list";



    String HOTPLUG_INTELLI_PLUG_ENABLE = "/sys/module/intelli_plug/parameters/intelli_plug_active";
    String HOTPLUG_INTELLI_PLUG_PROFILE = "/sys/module/intelli_plug/parameters/nr_run_profile_sel";
    String HOTPLUG_INTELLI_PLUG_ECO = "/sys/module/intelli_plug/parameters/eco_mode_active";
    String HOTPLUG_INTELLI_PLUG_TOUCH_BOOST = "/sys/module/intelli_plug/parameters/touch_boost_active";
    String HOTPLUG_INTELLI_PLUG_HYSTERESIS = "/sys/module/intelli_plug/parameters/nr_run_hysteresis";
    String HOTPLUG_INTELLI_PLUG_THRESHOLD = "/sys/module/intelli_plug/parameters/cpu_nr_run_threshold";
    String HOTPLUG_INTELLI_PLUG_SCREEN_OFF_MAX = "/sys/module/intelli_plug/parameters/screen_off_max";


    String HOTPLUG_INTELLI_PLUG_5_ENABLE = "/sys/kernel/intelli_plug/intelli_plug_active";
    String HOTPLUG_INTELLI_PLUG_5_DEBUG = "/sys/kernel/intelli_plug/debug_intelli_plug";
    String HOTPLUG_INTELLI_PLUG_5_PROFILE = "/sys/kernel/intelli_plug/full_mode_profile";
    String HOTPLUG_INTELLI_PLUG_5_SUSPEND = "/sys/kernel/intelli_plug/hotplug_suspend";
    String HOTPLUG_INTELLI_PLUG_5_CPUS_BOOSTED = "/sys/kernel/intelli_plug/cpus_boosted";
    String HOTPLUG_INTELLI_PLUG_5_HYSTERESIS = "/sys/kernel/intelli_plug/nr_run_hysteresis";
    String HOTPLUG_INTELLI_PLUG_5_MIN_CPUS_ONLINE = "/sys/kernel/intelli_plug/min_cpus_online";
    String HOTPLUG_INTELLI_PLUG_5_MAX_CPUS_ONLINE = "/sys/kernel/intelli_plug/max_cpus_online";
    String HOTPLUG_INTELLI_PLUG_5_MAX_CPUS_ONLINE_SUSP = "/sys/kernel/intelli_plug/max_cpus_online_susp";
    String HOTPLUG_INTELLI_PLUG_5_SUSPEND_DEFER_TIME = "/sys/kernel/intelli_plug/suspend_defer_time";
    String HOTPLUG_INTELLI_PLUG_5_DEFER_SAMPLING = "/sys/kernel/intelli_plug/def_sampling_ms";
    String HOTPLUG_INTELLI_PLUG_5_BOOST_LOCK_DURATION = "/sys/kernel/intelli_plug/boost_lock_duration";
    String HOTPLUG_INTELLI_PLUG_5_DOWN_LOCK_DURATION = "/sys/kernel/intelli_plug/down_lock_duration";
    String HOTPLUG_INTELLI_PLUG_5_THRESHOLD = "/sys/kernel/intelli_plug/cpu_nr_run_threshold";
    String HOTPLUG_INTELLI_PLUG_5_FSHIFT = "/sys/kernel/intelli_plug/nr_fshift";
    String HOTPLUG_INTELLI_PLUG_5_SCREEN_OFF_MAX = "/sys/kernel/intelli_plug/screen_off_max";




    String HOTPLUG_BLU_PLUG_ENABLE = "/sys/module/blu_plug/parameters/enabled";
    String HOTPLUG_BLU_PLUG_POWERSAVER_MODE = "/sys/module/blu_plug/parameters/powersaver_mode";
    String HOTPLUG_BLU_PLUG_MIN_ONLINE = "/sys/module/blu_plug/parameters/min_online";
    String HOTPLUG_BLU_PLUG_MAX_ONLINE = "/sys/module/blu_plug/parameters/max_online";
    String HOTPLUG_BLU_PLUG_MAX_CORES_SCREEN_OFF = "/sys/module/blu_plug/parameters/max_cores_screenoff";
    String HOTPLUG_BLU_PLUG_MAX_FREQ_SCREEN_OFF = "/sys/module/blu_plug/parameters/max_freq_screenoff";
    String HOTPLUG_BLU_PLUG_UP_THRESHOLD = "/sys/module/blu_plug/parameters/up_threshold";
    String HOTPLUG_BLU_PLUG_UP_TIMER_CNT = "/sys/module/blu_plug/parameters/up_timer_cnt";
    String HOTPLUG_BLU_PLUG_DOWN_TIMER_CNT = "/sys/module/blu_plug/parameters/down_timer_cnt";




    String HOTPLUG_MSM_ENABLE = "/sys/module/msm_hotplug/enabled";
    String HOTPLUG_MSM_ENABLE_2 = "/sys/module/msm_hotplug/msm_enabled";
    String HOTPLUG_MSM_DEBUG_MASK = "/sys/module/msm_hotplug/parameters/debug_mask";
    String HOTPLUG_MSM_MIN_CPUS_ONLINE = "/sys/module/msm_hotplug/min_cpus_online";
    String HOTPLUG_MSM_MAX_CPUS_ONLINE = "/sys/module/msm_hotplug/max_cpus_online";
    String HOTPLUG_MSM_CPUS_BOOSTED = "/sys/module/msm_hotplug/cpus_boosted";
    String HOTPLUG_MSM_MAX_CPUS_ONLINE_SUSP = "/sys/module/msm_hotplug/max_cpus_online_susp";
    String HOTPLUG_MSM_BOOST_LOCK_DURATION = "/sys/module/msm_hotplug/boost_lock_duration";
    String HOTPLUG_MSM_DOWN_LOCK_DURATION = "/sys/module/msm_hotplug/down_lock_duration";
    String HOTPLUG_MSM_HISTORY_SIZE = "/sys/module/msm_hotplug/history_size";
    String HOTPLUG_MSM_UPDATE_RATE = "/sys/module/msm_hotplug/update_rate";
    String HOTPLUG_MSM_UPDATE_RATES = "/sys/module/msm_hotplug/update_rates";
    String HOTPLUG_MSM_FAST_LANE_LOAD = "/sys/module/msm_hotplug/fast_lane_load";
    String HOTPLUG_MSM_FAST_LANE_MIN_FREQ = "/sys/module/msm_hotplug/fast_lane_min_freq";
    String HOTPLUG_MSM_OFFLINE_LOAD = "/sys/module/msm_hotplug/offline_load";
    String HOTPLUG_MSM_IO_IS_BUSY = "/sys/module/msm_hotplug/io_is_busy";
    String HOTPLUG_MSM_HP_IO_IS_BUSY = "/sys/module/msm_hotplug/hp_io_is_busy";
    String HOTPLUG_MSM_SUSPEND_MAX_CPUS = "/sys/module/msm_hotplug/suspend_max_cpus";
    String HOTPLUG_MSM_SUSPEND_FREQ = "/sys/module/msm_hotplug/suspend_freq";
    String HOTPLUG_MSM_SUSPEND_MAX_FREQ = "/sys/module/msm_hotplug/suspend_max_freq";
    String HOTPLUG_MSM_SUSPEND_DEFER_TIME = "/sys/module/msm_hotplug/suspend_defer_time";




    String MAKO_HOTPLUG_ENABLED = "/sys/class/misc/mako_hotplug_control/enabled";
    String MAKO_HOTPLUG_CORES_ON_TOUCH = "/sys/class/misc/mako_hotplug_control/cores_on_touch";
    String MAKO_HOTPLUG_CPUFREQ_UNPLUG_LIMIT = "/sys/class/misc/mako_hotplug_control/cpufreq_unplug_limit";
    String MAKO_HOTPLUG_FIRST_LEVEL = "/sys/class/misc/mako_hotplug_control/first_level";
    String MAKO_HOTPLUG_HIGH_LOAD_COUNTER = "/sys/class/misc/mako_hotplug_control/high_load_counter";
    String MAKO_HOTPLUG_LOAD_THRESHOLD = "/sys/class/misc/mako_hotplug_control/load_threshold";
    String MAKO_HOTPLUG_MAX_LOAD_COUNTER = "/sys/class/misc/mako_hotplug_control/max_load_counter";
    String MAKO_HOTPLUG_MIN_TIME_CPU_ONLINE = "/sys/class/misc/mako_hotplug_control/min_time_cpu_online";
    String MAKO_HOTPLUG_TIMER = "/sys/class/misc/mako_hotplug_control/timer";
    String MAKO_HOTPLUG_SUSPEND_FREQ = "/sys/class/misc/mako_hotplug_control/suspend_frequency";






    String ALUCARD_HOTPLUG_ENABLE = "/sys/kernel/alucard_hotplug/hotplug_enable";
    String ALUCARD_HOTPLUG_HP_IO_IS_BUSY = "/sys/kernel/alucard_hotplug/hp_io_is_busy";
    String ALUCARD_HOTPLUG_SAMPLING_RATE = "/sys/kernel/alucard_hotplug/hotplug_sampling_rate";
    String ALUCARD_HOTPLUG_SUSPEND = "/sys/kernel/alucard_hotplug/hotplug_suspend";
    String ALUCARD_HOTPLUG_MIN_CPUS_ONLINE = "/sys/kernel/alucard_hotplug/min_cpus_online";
    String ALUCARD_HOTPLUG_MAX_CORES_LIMIT = "/sys/kernel/alucard_hotplug/maxcoreslimit";
    String ALUCARD_HOTPLUG_MAX_CORES_LIMIT_SLEEP = "/sys/kernel/alucard_hotplug/maxcoreslimit_sleep";
    String ALUCARD_HOTPLUG_CPU_DOWN_RATE = "/sys/kernel/alucard_hotplug/cpu_down_rate";
    String ALUCARD_HOTPLUG_CPU_UP_RATE = "/sys/kernel/alucard_hotplug/cpu_up_rate";


    String LGE_TOUCH_DT2W = "/sys/devices/virtual/input/lge_touch/dt_wake_enabled";
    String LGE_TOUCH_CORE_DT2W = "/sys/module/lge_touch_core/parameters/doubletap_to_wake";
    String LGE_TOUCH_GESTURE = "/sys/devices/virtual/input/lge_touch/touch_gesture";
    String DT2W = "/sys/android_touch/doubletap2wake";
    String TOUCH_PANEL_DT2W = "/proc/touchpanel/double_tap_enable";
    String DT2W_WAKEUP_GESTURE = "/sys/devices/virtual/input/input1/wakeup_gesture";



    // S2W
    String S2W_ONLY = "/sys/android_touch/s2w_s2sonly";
    String SW2 = "/sys/android_touch/sweep2wake";



    // T2W
    String TSP_T2W = "/sys/devices/f9966000.i2c/i2c-1/1-004a/tsp";
    String TOUCHWAKE_T2W = "/sys/class/misc/touchwake/enabled";
}
