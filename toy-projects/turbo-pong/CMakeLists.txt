cmake_minimum_required (VERSION 2.8)
project (Pong)

################################
# GTest
################################
#find_package(GTest)
add_subdirectory(gtest-1.6.0)
enable_testing(true)

include_directories(src ${gtest_SOURCE_DIR}/include ${gtest_SOURCE_DIR})

################################
# Boost
################################
set(Boost_DEBUG 1)
set(Boost_USE_STATIC_LIBS OFF)
set(Boost_USE_MULTITHREADED ON)
set(Boost_USE_STATIC_RUNTIME ON)

find_package(Boost 1.58 COMPONENTS system thread)

if (Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIRS})
endif()

################################
# SDL
################################
find_package(SDL)
if (SDL_FOUND)
    include_directories(${SDL_INCLUDE_DIR})
endif()
find_package(SDL_ttf REQUIRED )
if (SDL_ttf_FOUND)
    include_directories(${SDL_ttf_INCLUDE_DIR})
endif()

################################
# Pong
################################
add_subdirectory(src)
add_subdirectory(graphics)
add_subdirectory(components)
add_subdirectory(test)
