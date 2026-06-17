add_library(libresolv SHARED
        "libresolv/resolv_stub.c")
target_include_directories(libresolv PUBLIC "libresolv")
target_compile_options(libresolv PRIVATE "-Wall" "-pipe" "-fPIC" "-DPIC" "-D_GNU_SOURCE")
set_target_properties(libresolv PROPERTIES OUTPUT_NAME "resolv")
