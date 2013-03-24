add_executable(unit-tests unitTests.cpp)
target_link_libraries(unit-tests gtest gtest_main Graphics Components)
add_test(unit-tests unit-tests)

